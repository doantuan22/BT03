package vn.iotstar.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/forgot-password")
public class ForgotPasswordController extends HttpServlet {

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
        if (email != null) {
            req.setAttribute("email", email.trim());
        }

        req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
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

        if (email == null || email.isBlank()) {
            req.setAttribute("alert", "Vui lòng nhập địa chỉ Email hoặc tên tài khoản.");
            req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
            return;
        }

        email = email.trim();
        req.setAttribute("email", email);

        boolean requested = userService.requestPasswordReset(email);
        if (requested) {
            resp.sendRedirect(req.getContextPath() + "/reset-password?email=" + URLEncoder.encode(email, StandardCharsets.UTF_8) + "&sent=true");
        } else {
            req.setAttribute("alert", "Email hoặc tên tài khoản không tồn tại trong hệ thống.");
            req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
        }
    }
}
