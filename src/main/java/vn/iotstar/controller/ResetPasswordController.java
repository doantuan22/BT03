package vn.iotstar.controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.model.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/reset-password")
public class ResetPasswordController extends HttpServlet {

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("account") != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }

        String email = req.getParameter("email");
        String sent = req.getParameter("sent");

        if (email != null) {
            req.setAttribute("email", email.trim());
        }
        if ("true".equals(sent)) {
            req.setAttribute("successMsg", "Mã xác thực OTP đã được gửi đến email của bạn. Vui lòng nhập mã và mật khẩu mới.");
        }

        req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        if (email == null || email.isBlank()) {
            email = req.getParameter("username");
        }

        String otp = req.getParameter("otp");
        if (otp == null || otp.isBlank()) {
            otp = req.getParameter("OTP");
        }
        if (otp == null || otp.isBlank()) {
            otp = req.getParameter("otp_code");
        }
        if (otp == null || otp.isBlank()) {
            otp = req.getParameter("otpCode");
        }

        String password = req.getParameter("password");
        if (password == null || password.isBlank()) {
            password = req.getParameter("newPassword");
        }

        String passwordConfirmation = req.getParameter("password_confirmation");
        if (passwordConfirmation == null || passwordConfirmation.isBlank()) {
            passwordConfirmation = req.getParameter("confirmPassword");
        }
        if (passwordConfirmation == null || passwordConfirmation.isBlank()) {
            passwordConfirmation = req.getParameter("passwordConfirmation");
        }
        if (passwordConfirmation == null || passwordConfirmation.isBlank()) {
            passwordConfirmation = req.getParameter("confirm_password");
        }

        String action = req.getParameter("action");

        if (email != null) {
            req.setAttribute("email", email.trim());
        }

        if ("resend".equalsIgnoreCase(action)) {
            if (email == null || email.isBlank()) {
                req.setAttribute("alert", "Vui lòng nhập email để nhận lại mã OTP.");
                req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
                return;
            }

            boolean resendSuccess = userService.requestPasswordReset(email.trim());
            if (resendSuccess) {
                req.setAttribute("successMsg", "Mã OTP mới đã được gửi tới email của bạn.");
            } else {
                req.setAttribute("alert", "Không tìm thấy tài khoản với email này.");
            }
            req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
            return;
        }

        if (email == null || email.isBlank() || otp == null || otp.isBlank()
                || password == null || password.isBlank()) {
            req.setAttribute("alert", "Vui lòng điền đầy đủ tất cả các thông tin.");
            req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
            return;
        }

        if (passwordConfirmation != null && !password.equals(passwordConfirmation)) {
            req.setAttribute("alert", "Mật khẩu xác nhận không khớp. Vui lòng kiểm tra lại.");
            req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
            return;
        }

        email = email.trim();
        otp = otp.trim();

        User user = userService.findByEmail(email);
        if (user == null) {
            user = userService.get(email);
        }

        if (user == null) {
            req.setAttribute("alert", "Tài khoản không tồn tại.");
            req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
            return;
        }

        if (user.getOtpCode() == null || !user.getOtpCode().equals(otp)) {
            req.setAttribute("alert", "Mã OTP không chính xác. Vui lòng kiểm tra lại.");
            req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
            return;
        }

        if (user.getOtpExpiresAt() == null || user.getOtpExpiresAt().before(new java.util.Date())) {
            req.setAttribute("alert", "Mã OTP đã hết hạn. Vui lòng nhấn 'Gửi lại OTP' để nhận mã mới.");
            req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
            return;
        }

        boolean success = userService.resetPassword(email, otp, password);
        if (success) {
            resp.sendRedirect(req.getContextPath() + "/login?reset=true");
        } else {
            req.setAttribute("alert", "Lỗi thiết lập lại mật khẩu. Vui lòng thử lại sau.");
            req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
        }
    }
}
