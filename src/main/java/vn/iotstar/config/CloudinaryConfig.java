package vn.iotstar.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.InputStream;
import java.util.Properties;

public class CloudinaryConfig {
    private static Cloudinary cloudinary;

    static {
        try {
            Properties properties = new Properties();
            InputStream is = CloudinaryConfig.class.getClassLoader().getResourceAsStream("cloudinary.properties");
            if (is != null) {
                properties.load(is);
                cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", properties.getProperty("CLOUD_NAME"),
                        "api_key", properties.getProperty("API_KEY"),
                        "api_secret", properties.getProperty("API_SECRET"),
                        "timeout", 60000,
                        "connection_timeout", 30000
                ));
            } else {
                System.err.println("Warning: cloudinary.properties not found in classpath.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error initializing Cloudinary: " + e.getMessage());
        }
    }

    public static Cloudinary getCloudinary() {
        return cloudinary;
    }
}
