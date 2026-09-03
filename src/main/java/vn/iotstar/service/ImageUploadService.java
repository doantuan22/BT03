package vn.iotstar.service;

import vn.iotstar.model.ImageUploadResult;

import java.io.InputStream;

public interface ImageUploadService {
    /**
     * Upload an image to Cloudinary.
     * @param fileData the file data as InputStream
     * @param originalFileName the original file name
     * @return ImageUploadResult containing URL and Public ID, or null if upload fails
     */
    ImageUploadResult uploadImage(InputStream fileData, String originalFileName);

    /**
     * Delete an image from Cloudinary by its public ID.
     * @param publicId the public ID of the image
     * @return true if successfully deleted, false otherwise
     */
    boolean deleteImage(String publicId);
}
