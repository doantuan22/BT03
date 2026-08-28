package vn.iotstar.filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.model.User;
import vn.iotstar.util.Constant;

@WebFilter(urlPatterns = { "/admin/*", "/manager/*" })
public class AuthorizationFilter implements Filter {
    @Override
    public void doFilter(jakarta.servlet.ServletRequest request, jakarta.servlet.ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);
        User account = session == null ? null : (User) session.getAttribute("account");

        String path = req.getRequestURI().substring(req.getContextPath().length());

        if (path.startsWith("/admin/")) {
            if (account == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            if (account.getRoleid() != Constant.ROLE_ADMIN) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        if (path.startsWith("/manager/")) {
            if (account == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
            if (account.getRoleid() != Constant.ROLE_MANAGER) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
