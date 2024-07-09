package com.example.myproject.service;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.Image;
import com.example.myproject.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    public void save(Image image) {
        imageRepository.save(image);
    }

}
