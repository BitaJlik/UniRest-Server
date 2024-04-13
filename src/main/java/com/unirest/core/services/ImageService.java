package com.unirest.core.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("unused")
@Service
public class ImageService {

    private static final String UPLOAD_DIR = "./uploads/";
    private static final String[] extensions = {"png", "jpg"};

    public void saveImage(String folder, String fileName, MultipartFile image) {
        try {
            String fileExtension = StringUtils.getFilenameExtension(image.getOriginalFilename());

            if (fileExtension == null) {
                fileExtension = "";
            } else {
                fileExtension = "." + fileExtension;
            }

            String fileNameWithExtension = fileName + fileExtension;

            Path uploadDir = Paths.get(UPLOAD_DIR, folder);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(fileNameWithExtension);
            Files.write(filePath, image.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getImageById(String folder, String imageName) {
        try {
            for (String extension : extensions) {
                Path imagePath = Paths.get(UPLOAD_DIR, folder, imageName + "." + extension);
                if (Files.exists(imagePath)) {
                    return Files.readAllBytes(imagePath);
                } else {
                    System.out.println("File not found: " + imagePath);
                }
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
