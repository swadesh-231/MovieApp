package com.movieapp.service;


import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;

public interface FileService {
    String uploadFile(String path, MultipartFile file);
    InputStream getResourceFile(String path, String filename);
    void deleteFile(String path, String filename);
}