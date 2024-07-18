package com.example.myproject.service;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.Photo;
import com.example.myproject.domain.User;
import com.example.myproject.repository.BoardRepository;
import com.example.myproject.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PhotoService {
    private final PhotoRepository photoRepository;

   public Photo savePhoto(MultipartFile file) throws IOException {
       Photo photo = new Photo();
       String uploadDir = "photos/";
       photo.setFileName(file.getOriginalFilename());
       photo.setFileType(file.getContentType());
       photo.setFilePath(uploadDir+file.getOriginalFilename());
       photo.setData(file.getBytes());
       return photoRepository.save(photo);
   }

   public Photo getPhotoById(Long id) {
       return photoRepository.findById(id).orElse(null);
   }
}
