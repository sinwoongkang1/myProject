package com.example.myproject.controller;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.Image;
import com.example.myproject.domain.Photo;
import com.example.myproject.domain.User;
import com.example.myproject.service.BoardService;
import com.example.myproject.service.ImageService;
import com.example.myproject.service.PhotoService;
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
private final PhotoService photoService;
private final BoardService boardService;
private final ImageService imageService;
private final UserService userService;

    @PostMapping("/uploadProfilePicture")
    public String uploadProfilePicture(@RequestParam("file") MultipartFile file,
                                                       @CookieValue(value = "username", defaultValue = "") String username,
                                                       Model model) {
        if (username.isEmpty()) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username is missing in the cookie");
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
            ResponseEntity.status(HttpStatus.OK).body(filePath);
            return "redirect:/BBelog/profile/";

        } catch (IOException e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile picture");
        }
        return "redirect:/BBelog/profile/";
    }

//    @GetMapping("/profilePicture/{id}")
//    @ResponseBody
//    public byte[] getProfilePicture(@PathVariable Long id) {
//        Image image = imageService.getImage(id);
//        return image.getData();
//    }

    @PostMapping("/uploadPhoto")
    public String uploadPhoto (@RequestParam("file") MultipartFile file,
                               @CookieValue(value = "username", defaultValue = "") String username,
                               @ModelAttribute Board board,
                               Model model) {

        if (username.isEmpty()) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유저 정보 없음");
        }
        try {
            User user = userService.findByUsername(username);
            Photo photo = photoService.savePhoto(file);

            board.setPhoto(photo);
            board.setTitle("temporary");
            board.setContent("temporary");
            board.setUser(user);
            boardService.save(board);


            String filePath = "photos/" + photo.getFileName();
            photo.setFilePath(filePath);


            String uploadDir = new File("photos").getAbsolutePath();
            file.transferTo(new File(uploadDir + "/" + photo.getFileName()));

            board.setPhoto(photo);
            boardService.save(board);
            model.addAttribute("board", board);
            ResponseEntity.status(HttpStatus.OK).body(filePath);
            return "redirect:/BBelog/write";

        } catch (IOException e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload profile picture");
        }
        return "redirect:/BBelog/write";
    }
//    @GetMapping("/photos/{id}")
//    @ResponseBody
//    public byte[] getContentPhoto(@PathVariable Long id) {
//        Photo photo = photoService.getPhotoById(id);
//        return photo.getData();
//    }
}
