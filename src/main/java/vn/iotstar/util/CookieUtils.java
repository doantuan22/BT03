package vn.iotstar.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.iotstar.model.User;
import vn.iotstar.service.UserService;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

public class CookieUtils {

    private static final String SECRET_SALT = "IotStarShoppingSecretKey_2026";

    public static void saveRememberMe(HttpServletRequest req, HttpServletResponse resp, User user) {
        if (user == null || user.getUsername() == null) {
            return;
        }

        String signature = generateSignature(user.getUsername(), user.getPassword());
        String rawToken = user.getUsername() + ":" + signature;
        String encodedToken = Base64.getUrlEncoder().withoutPadding().encodeToString(rawToken.getBytes(StandardCharsets.UTF_8));

        Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, encodedToken);
        cookie.setMaxAge(Constant.COOKIE_REMEMBER_EXPIRE);
        cookie.setHttpOnly(true);
        cookie.setPath(getCookiePath(req));
        resp.addCookie(cookie);

        clearLegacyCookie(req, resp);
    }

    public static void deleteRememberMe(HttpServletRequest req, HttpServletResponse resp) {
        Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath(getCookiePath(req));
        resp.addCookie(cookie);

        Cookie rootCookie = new Cookie(Constant.COOKIE_REMEMBER, "");
        rootCookie.setMaxAge(0);
        rootCookie.setHttpOnly(true);
        rootCookie.setPath("/");
        resp.addCookie(rootCookie);

        clearLegacyCookie(req, resp);
    }

    public static User validateRememberMe(HttpServletRequest req, UserService userService) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null || userService == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (Constant.COOKIE_REMEMBER.equals(cookie.getName())) {
                String token = cookie.getValue();
                if (token == null || token.trim().isEmpty()) {
                    continue;
                }

                try {
                    String decoded = new String(Base64.getUrlDecoder().decode(token), StandardCharsets.UTF_8);
                    int separatorIdx = decoded.indexOf(':');
                    if (separatorIdx <= 0) {
                        continue;
                    }

                    String username = decoded.substring(0, separatorIdx);
                    String signature = decoded.substring(separatorIdx + 1);

                    User user = userService.get(username);
                    if (user != null && user.isActivated()) {
                        String expectedSig = generateSignature(user.getUsername(), user.getPassword());
                        if (expectedSig.equals(signature)) {
                            return user;
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    private static void clearLegacyCookie(HttpServletRequest req, HttpServletResponse resp) {
        Cookie old = new Cookie("username", "");
        old.setMaxAge(0);
        old.setHttpOnly(true);
        old.setPath(getCookiePath(req));
        resp.addCookie(old);

        Cookie oldRoot = new Cookie("username", "");
        oldRoot.setMaxAge(0);
        oldRoot.setHttpOnly(true);
        oldRoot.setPath("/");
        resp.addCookie(oldRoot);
    }

    private static String getCookiePath(HttpServletRequest req) {
        String path = req.getContextPath();
        if (path == null || path.trim().isEmpty()) {
            return "/";
        }
        return path;
    }

    private static String generateSignature(String username, String password) {
        try {
            String raw = username + "|" + (password == null ? "" : password) + "|" + SECRET_SALT;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
