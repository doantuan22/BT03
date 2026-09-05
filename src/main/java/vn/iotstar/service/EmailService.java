package vn.iotstar.service;

public interface EmailService {
    boolean sendActivationEmail(String toEmail, String fullName, String otpCode);

    boolean sendPasswordResetEmail(String toEmail, String fullName, String otpCode);

    boolean sendEmail(String toEmail, String subject, String contentHtml);
}
