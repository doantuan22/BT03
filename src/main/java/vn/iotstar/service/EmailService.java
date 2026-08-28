package vn.iotstar.service;

public interface EmailService {
    /**
     * Sends an account activation email containing the 6-digit OTP code.
     *
     * @param toEmail   Recipient email address
     * @param fullName  Recipient full name
     * @param otpCode   6-digit OTP code
     * @return true if email was successfully sent or queued, false otherwise
     */
    boolean sendActivationEmail(String toEmail, String fullName, String otpCode);

    /**
     * Sends a password reset email containing the 6-digit OTP code.
     *
     * @param toEmail   Recipient email address
     * @param fullName  Recipient full name
     * @param otpCode   6-digit OTP code
     * @return true if email was successfully sent or queued, false otherwise
     */
    boolean sendPasswordResetEmail(String toEmail, String fullName, String otpCode);

    /**
     * Sends a generic email with custom subject and HTML body.
     *
     * @param toEmail      Recipient email address
     * @param subject      Subject line
     * @param contentHtml  HTML message body
     * @return true if sent, false otherwise
     */
    boolean sendEmail(String toEmail, String subject, String contentHtml);
}
