package com.unirest.core.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("unused")
@Service
public class FileService {

    private static final String UPLOAD_DIR = "./uploads/";

    public void saveFile(String folder, String fileName, String fileContent,String ext) {
        try {
            String fileNameWithExtension = fileName + ext;

            Path uploadDir = Paths.get(UPLOAD_DIR, folder);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            Path filePath = uploadDir.resolve(fileNameWithExtension);
            Files.write(filePath, fileContent.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}