package com.example.myproject.service;

import com.example.myproject.domain.Photo;
import com.example.myproject.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;

   public Photo savePhoto(Photo photo) {
       return photoRepository.save(photo);
   }
}
