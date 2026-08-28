package vn.iotstar.service.impl;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import vn.iotstar.service.EmailService;
import vn.iotstar.util.Constant;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = Logger.getLogger(EmailServiceImpl.class.getName());

    @Override
    public boolean sendActivationEmail(String toEmail, String fullName, String otpCode) {
        String subject = "Xác thực tài khoản IoTStar - Mã OTP của bạn";
        String contentHtml = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f4f6f8; margin: 0; padding: 20px; }
                    .container { max-width: 500px; margin: 0 auto; background: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.08); }
                    .header { text-align: center; border-bottom: 2px solid #2563eb; padding-bottom: 15px; margin-bottom: 20px; }
                    .header h2 { color: #2563eb; margin: 0; }
                    .otp-box { text-align: center; margin: 25px 0; }
                    .otp-code { display: inline-block; font-size: 32px; font-weight: bold; letter-spacing: 8px; color: #1e40af; background: #eff6ff; padding: 12px 24px; border-radius: 8px; border: 1px dashed #3b82f6; }
                    .footer { text-align: center; color: #6b7280; font-size: 12px; margin-top: 30px; border-top: 1px solid #e5e7eb; padding-top: 15px; }
                    .warning { color: #dc2626; font-size: 13px; margin-top: 10px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h2>IoTStar</h2>
                    </div>
                    <p>Xin chào <strong>""" + (fullName != null ? fullName : "bạn") + """
                    </strong>,</p>
                    <p>Cảm ơn bạn đã đăng ký tài khoản tại IoTStar. Để hoàn tất đăng ký và kích hoạt tài khoản, vui lòng sử dụng mã OTP dưới đây:</p>
                    <div class="otp-box">
                        <span class="otp-code">""" + otpCode + """
                        </span>
                    </div>
                    <p class="warning">Lưu ý: Mã OTP này có hiệu lực trong vòng <strong>""" + Constant.OTP_EXPIRY_MINUTES + """
                     phút</strong>. Không chia sẻ mã này cho bất kỳ ai.</p>
                    <p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email.</p>
                    <div class="footer">
                        <p>&copy; IoTStar System - Automated Email. Please do not reply.</p>
                    </div>
                </div>
            </body>
            </html>
            """;

        return sendEmail(toEmail, subject, contentHtml);
    }

    @Override
    public boolean sendPasswordResetEmail(String toEmail, String fullName, String otpCode) {
        String subject = "Đặt lại mật khẩu IoTStar - Mã xác thực OTP";
        String contentHtml = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; background-color: #f4f6f8; margin: 0; padding: 20px; }
                    .container { max-width: 500px; margin: 0 auto; background: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.08); }
                    .header { text-align: center; border-bottom: 2px solid #ef4444; padding-bottom: 15px; margin-bottom: 20px; }
                    .header h2 { color: #ef4444; margin: 0; }
                    .otp-box { text-align: center; margin: 25px 0; }
                    .otp-code { display: inline-block; font-size: 32px; font-weight: bold; letter-spacing: 8px; color: #b91c1c; background: #fef2f2; padding: 12px 24px; border-radius: 8px; border: 1px dashed #f87171; }
                    .footer { text-align: center; color: #6b7280; font-size: 12px; margin-top: 30px; border-top: 1px solid #e5e7eb; padding-top: 15px; }
                    .warning { color: #dc2626; font-size: 13px; margin-top: 10px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h2>IoTStar</h2>
                    </div>
                    <p>Xin chào <strong>""" + (fullName != null ? fullName : "bạn") + """
                    </strong>,</p>
                    <p>Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn. Vui lòng sử dụng mã OTP dưới đây để hoàn tất việc thiết lập lại mật khẩu:</p>
                    <div class="otp-box">
                        <span class="otp-code">""" + otpCode + """
                        </span>
                    </div>
                    <p class="warning">Lưu ý: Mã OTP này có hiệu lực trong vòng <strong>""" + Constant.OTP_EXPIRY_MINUTES + """
                     phút</strong>. Không chia sẻ mã này cho bất kỳ ai để bảo vệ an toàn tài khoản.</p>
                    <p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email và mật khẩu của bạn sẽ không thay đổi.</p>
                    <div class="footer">
                        <p>&copy; IoTStar System - Automated Email. Please do not reply.</p>
                    </div>
                </div>
            </body>
            </html>
            """;

        return sendEmail(toEmail, subject, contentHtml);
    }

    @Override
    public boolean sendEmail(String toEmail, String subject, String contentHtml) {
        if (toEmail == null || toEmail.isBlank()) {
            LOGGER.warning("Recipient email is empty, skipping email dispatch.");
            return false;
        }

        // Print to log for immediate visibility and testing
        LOGGER.info(String.format("Sending Email to [%s] | Subject: [%s]", toEmail, subject));

        String host = Constant.SMTP_HOST;
        String port = Constant.SMTP_PORT;
        String username = Constant.SMTP_USERNAME;
        String password = Constant.SMTP_PASSWORD;

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", username != null && !username.isBlank() ? "true" : "false");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");

        Session session;
        if (username != null && !username.isBlank() && password != null && !password.isBlank()) {
            session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
        } else {
            session = Session.getInstance(props);
        }

        try {
            MimeMessage message = new MimeMessage(session);
            try {
                message.setFrom(new InternetAddress(Constant.SMTP_FROM_EMAIL, Constant.SMTP_FROM_NAME, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                message.setFrom(new InternetAddress(Constant.SMTP_FROM_EMAIL));
            }
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject, "UTF-8");
            message.setContent(contentHtml, "text/html; charset=UTF-8");

            // Attempt actual SMTP send if configured
            if (username != null && !username.isBlank()) {
                Transport.send(message);
                LOGGER.info("Email successfully delivered to " + toEmail);
            } else {
                LOGGER.info("SMTP credentials not configured. Email logged to console (Simulation Mode). Target: " + toEmail);
            }
            return true;
        } catch (MessagingException e) {
            LOGGER.log(Level.WARNING, "Failed to send email via SMTP to " + toEmail + ": " + e.getMessage(), e);
            // Return true so user flow is not broken in offline environments
            return false;
        }
    }
}
