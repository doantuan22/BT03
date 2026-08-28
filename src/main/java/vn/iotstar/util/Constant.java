package vn.iotstar.util;

public class Constant {
    public static final String DIR = "E:\\upload";

    public static final String SESSION_USERNAME = "username";
    public static final String COOKIE_REMEMBER = "username";
    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_MANAGER = 2;

    // OTP Configuration
    public static final int OTP_EXPIRY_MINUTES = 5;

    // Email SMTP Configuration
    public static final String SMTP_HOST = System.getProperty("smtp.host", System.getenv().getOrDefault("SMTP_HOST", "smtp.gmail.com"));
    public static final String SMTP_PORT = System.getProperty("smtp.port", System.getenv().getOrDefault("SMTP_PORT", "587"));
    public static final String SMTP_USERNAME = "doantuan0947881956@gmail.com";
    public static final String SMTP_PASSWORD = "agvh nhwx nvkx sxwa";
    public static final String SMTP_FROM_EMAIL = System.getProperty("smtp.from", System.getenv().getOrDefault("SMTP_FROM", "no-reply@iotstar.vn"));
    public static final String SMTP_FROM_NAME = "IoTStar Support";

    public static class Path {
        public static final String LOGIN            = "/views/login.jsp";
        public static final String REGISTER         = "/views/register.jsp";
        public static final String VERIFY_OTP       = "/views/verify-otp.jsp";
        public static final String ACTIVATE_ACCOUNT = "/views/activate-account.jsp";
        public static final String FORGOT_PASSWORD      = "/views/forgot-password.jsp";
        public static final String RESET_PASSWORD       = "/views/reset-password.jsp";
        public static final String ADMIN_PRODUCT_LIST   = "/views/admin/list-product.jsp";
        public static final String ADMIN_PRODUCT_ADD    = "/views/admin/add-product.jsp";
        public static final String ADMIN_PRODUCT_EDIT   = "/views/admin/edit-product.jsp";
        public static final String ADMIN_PRODUCT_SHOW   = "/views/admin/show-product.jsp";
    }
}
