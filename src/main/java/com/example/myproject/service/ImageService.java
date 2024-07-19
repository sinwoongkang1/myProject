package com.example.myproject.service;

import com.example.myproject.domain.Image;
import com.example.myproject.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public Image saveImage(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setData(file.getBytes());
        return imageRepository.save(image);
    }
}
