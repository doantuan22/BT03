package vn.iotstar.service;

import vn.iotstar.model.ImageUploadResult;

import java.io.InputStream;

public interface ImageUploadService {
    ImageUploadResult uploadImage(InputStream fileData, String originalFileName);

    boolean deleteImage(String publicId);
}
