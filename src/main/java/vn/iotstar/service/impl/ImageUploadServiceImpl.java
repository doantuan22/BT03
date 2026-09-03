package vn.iotstar.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import vn.iotstar.config.CloudinaryConfig;
import vn.iotstar.model.ImageUploadResult;
import vn.iotstar.service.ImageUploadService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

public class ImageUploadServiceImpl implements ImageUploadService {

    private final Cloudinary cloudinary;

    public ImageUploadServiceImpl() {
        this.cloudinary = CloudinaryConfig.getCloudinary();
    }

    @Override
    public ImageUploadResult uploadImage(InputStream fileData, String originalFileName) {
        if (cloudinary == null) {
            System.err.println("Cloudinary is not initialized. Cannot upload image.");
            return null;
        }

        File tempFile = null;
        try {
            String uniqueId = UUID.randomUUID().toString();
            String publicId = "upload_" + uniqueId;

            String extension = ".tmp";
            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf('.')).toLowerCase();
            }

            tempFile = File.createTempFile("cld_upload_", extension);
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fileData.transferTo(fos);
            }

            Map uploadResult = null;
            Exception lastException = null;

            for (int attempt = 1; attempt <= 3; attempt++) {
                try {
                    uploadResult = cloudinary.uploader().upload(tempFile, ObjectUtils.asMap(
                            "public_id", publicId,
                            "overwrite", true,
                            "resource_type", "auto"
                    ));
                    if (uploadResult != null && uploadResult.get("secure_url") != null) {
                        break;
                    }
                } catch (Exception ex) {
                    lastException = ex;
                    System.err.printf("Cloudinary upload attempt #%d failed: %s%n", attempt, ex.getMessage());
                    if (attempt < 3) {
                        try {
                            Thread.sleep(1500L * attempt);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }
            }

            if (uploadResult == null) {
                if (lastException != null) {
                    lastException.printStackTrace();
                }
                return null;
            }

            String secureUrl = (String) uploadResult.get("secure_url");
            String resultPublicId = (String) uploadResult.get("public_id");

            return new ImageUploadResult(secureUrl, resultPublicId);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error uploading image to Cloudinary: " + e.getMessage());
            return null;
        } finally {
            if (tempFile != null && tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    @Override
    public boolean deleteImage(String publicId) {
        if (cloudinary == null) {
            System.err.println("Cloudinary is not initialized. Cannot delete image.");
            return false;
        }
        if (publicId == null || publicId.trim().isEmpty()) {
            return false;
        }

        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error deleting image from Cloudinary (publicId: " + publicId + "): " + e.getMessage());
            return false;
        }
    }
}
