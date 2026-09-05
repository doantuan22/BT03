package vn.iotstar.controller;

import java.io.IOException;
import java.util.Date;
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
@WebServlet(urlPatterns = {"/activate-account", "/verify-otp"})
public class ActivateAccountController extends HttpServlet {

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
        if (email == null || email.isBlank()) {
            email = req.getParameter("username");
        }

        String registered = req.getParameter("registered");

        if (email != null) {
            req.setAttribute("email", email.trim());
            req.setAttribute("username", email.trim());
        }
        if ("true".equals(registered)) {
            req.setAttribute("successMsg", "Đăng ký thành công! Vui lòng kiểm tra email để lấy mã OTP kích hoạt tài khoản.");
        }

        req.getRequestDispatcher(Constant.Path.ACTIVATE_ACCOUNT).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        String email = req.getParameter("email");
        if (email == null || email.isBlank()) {
            email = req.getParameter("username");
        }

        String otp = req.getParameter("otp");
        if (otp == null || otp.isBlank()) {
            otp = req.getParameter("otp_code");
        }
        if (otp == null || otp.isBlank()) {
            otp = req.getParameter("otpCode");
        }

        if (email != null) {
            req.setAttribute("email", email.trim());
            req.setAttribute("username", email.trim());
        }

        if ("resend".equalsIgnoreCase(action)) {
            if (email == null || email.isBlank()) {
                req.setAttribute("alert", "Vui lòng nhập email hoặc tên tài khoản để gửi lại mã OTP.");
                req.getRequestDispatcher(Constant.Path.ACTIVATE_ACCOUNT).forward(req, resp);
                return;
            }

            boolean resendSuccess = userService.resendOtp(email.trim());
            if (resendSuccess) {
                req.setAttribute("successMsg", "Mã OTP mới đã được gửi tới email của bạn.");
            } else {
                req.setAttribute("alert", "Không thể gửi lại mã OTP. Email/Tài khoản không tồn tại hoặc đã được kích hoạt.");
            }
            req.getRequestDispatcher(Constant.Path.ACTIVATE_ACCOUNT).forward(req, resp);
            return;
        }

        if (email == null || email.isBlank() || otp == null || otp.isBlank()) {
            req.setAttribute("alert", "Vui lòng nhập đầy đủ Email và mã OTP 6 chữ số.");
            req.getRequestDispatcher(Constant.Path.ACTIVATE_ACCOUNT).forward(req, resp);
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
            req.getRequestDispatcher(Constant.Path.ACTIVATE_ACCOUNT).forward(req, resp);
            return;
        }

        if (user.isActivated()) {
            resp.sendRedirect(req.getContextPath() + "/login?activated=true");
            return;
        }

        if (user.getOtpCode() == null || !user.getOtpCode().equals(otp)) {
            req.setAttribute("alert", "Mã OTP không chính xác. Vui lòng kiểm tra lại.");
            req.getRequestDispatcher(Constant.Path.ACTIVATE_ACCOUNT).forward(req, resp);
            return;
        }

        if (user.getOtpExpiresAt() == null || user.getOtpExpiresAt().before(new Date())) {
            req.setAttribute("alert", "Mã OTP đã hết hạn. Vui lòng bấm 'Gửi lại OTP' để nhận mã mới.");
            req.getRequestDispatcher(Constant.Path.ACTIVATE_ACCOUNT).forward(req, resp);
            return;
        }

        boolean activated = userService.activateAccount(email, otp);
        if (activated) {
            resp.sendRedirect(req.getContextPath() + "/login?activated=true");
        } else {
            req.setAttribute("alert", "Kích hoạt tài khoản không thành công. Vui lòng thử lại.");
            req.getRequestDispatcher(Constant.Path.ACTIVATE_ACCOUNT).forward(req, resp);
        }
    }
}
