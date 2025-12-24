package com.movieapp.service.impl;


import com.movieapp.exception.FileStorageException;
import com.movieapp.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFile(String path, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("File is empty or missing");
        }
        try {
            Path directoryPath = Paths.get(path);
            Files.createDirectories(directoryPath);
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isBlank()) {
                throw new FileStorageException("Invalid file name");
            }
            String fileName = System.currentTimeMillis() + "_" +
                    Paths.get(originalFilename).getFileName().toString();

            Path filePath = directoryPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (IOException e) {
            throw new FileStorageException("Failed to upload file", e);
        }
    }

    @Override
    public InputStream getResourceFile(String path, String fileName) {

        try {
            Path filePath = Paths.get(path).resolve(fileName).normalize();

            if (!Files.exists(filePath)) {
                throw new FileStorageException("File not found: " + fileName);
            }

            return Files.newInputStream(filePath);

        } catch (IOException e) {
            throw new FileStorageException("Failed to read file: " + fileName, e);
        }
    }

    @Override
    public void deleteFile(String path, String filename) {
        try {
            Files.deleteIfExists(Paths.get(path).resolve(filename));
        } catch (IOException e) {
            throw new FileStorageException("Failed to delete file: " + filename, e);
        }
    }
}

