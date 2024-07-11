package com.example.myproject.controller;

import com.example.myproject.domain.Image;
import com.example.myproject.domain.User;
import com.example.myproject.service.ImageService;
import com.example.myproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@Controller
@RequestMapping("/BBelog")
@RequiredArgsConstructor
public class ImageController {

private final ImageService imageService;
private final UserService userService;

    @PostMapping("/uploadProfilePicture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file,
                                                       @CookieValue(value = "username", defaultValue = "") String username,
                                                       Model model) {
        if (username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is missing in the cookie");
        }
        try {

            Image image = imageService.saveImage(file);
            User user = userService.findByUsername(username);
            String filePath = "images/" + image.getFileName();
            image.setFilePath(filePath);


            String uploadDir = new File("src/main/resources/static/images").getAbsolutePath();
            file.transferTo(new File(uploadDir + "/" + image.getFileName()));

            user.setProfileImage(image);
            userService.saveUser(user);
            model.addAttribute("user", user);
            return ResponseEntity.status(HttpStatus.OK).body(filePath);


        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile picture");
        }
    }

    @GetMapping("/profilePicture/{id}")
    @ResponseBody
    public byte[] getProfilePicture(@PathVariable Long id) {
        Image image = imageService.getImage(id);
        return image.getData();
    }

}
