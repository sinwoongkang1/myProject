package com.example.myproject.controller;

import com.example.myproject.domain.Admin;
import com.example.myproject.domain.Board;
import com.example.myproject.domain.User;
import com.example.myproject.repository.UserRepository;
import com.example.myproject.service.AdminService;
import com.example.myproject.service.BoardService;
import com.example.myproject.service.ImageService;
import com.example.myproject.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/BBelog")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AdminService adminService;
    private final BoardService boardService;
    private final UserRepository userRepository;
    private final ImageService imageService;

    @GetMapping
    public String BBelogIndex(Model model) {
        List<Board> boards = boardService.findByTemporaryFalse();
        model.addAttribute("boards", boards);
        return "/main/BBelog";
    }

    @GetMapping("/join")
    public String join() {
        return "/main/join";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        if (userService.existsByUsername(user.getUsername())) {
            redirectAttributes.addFlashAttribute("error", "이미 존재하는 ID입니다. 다른 ID를 사용해주세요.");
            return "redirect:/BBelog/join";
        } else {
            userService.saveUser(user);
            Admin admin = new Admin(user);
            adminService.saveAdmin(admin);
            return "redirect:/BBelog";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "/main/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpSession session, RedirectAttributes redirectAttributes
    ,HttpServletResponse response) {
        User foundUser = userService.findUserByUsername(user.getUsername());
        if (foundUser != null && user.getPassword().equals(foundUser.getPassword())) {
            Cookie cookie = new Cookie("username", foundUser.getUsername());
            cookie.setPath("/");
            response.addCookie(cookie);
            session.setAttribute("username", foundUser.getUsername());
            return "redirect:/BBelog";
        } else {
            redirectAttributes.addAttribute("error", true);
            return "redirect:/BBelog/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/BBelog";
    }

    @GetMapping("/profile")
    public String profile(Model model, @CookieValue(value = "username", defaultValue = "") String username) {
        if (username.isEmpty()) {
            return "redirect:/BBelog/main/login";
        }
        User user = userService.findUserByUsername(username);
        int myCal = 10000;
        if (user == null) {
            return "redirect:/BBelog";
        }
            List<Board> boards = boardService.findByBoardUserAndTemporaryFalse(user);
            int boardSize = boards.size();
            int cal = myCal - (500*boardSize);
            user.setCal(cal);
            userService.saveUser(user);
            boards.sort(Comparator.comparing(Board::getWriteTime).reversed());
            model.addAttribute("user", user);
            model.addAttribute("boards", boards);
            model.addAttribute("cal", cal);
            return "/main/profile";

    }

    @PostMapping("/uploadProfilePicture")
    public ResponseEntity<Map<String, String>> uploadProfilePicture(@RequestParam("profilePicture") MultipartFile file,
                                                                    @RequestParam String username) {
        String uploadDir = "/Users/kang/Documents/upload/";
        Map<String, String> response = new HashMap<>();
        try {
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }
            String fileName = file.getOriginalFilename();
            File destinationFile = new File(uploadDirFile, fileName);
            file.transferTo(destinationFile); // MultipartFile을 파일로 저장

            // 이미지 경로 설정
            String imagePath = "/uploads/" + fileName;

            // User 엔티티를 찾거나 생성합니다
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Image 엔티티 생성 및 User 엔티티에 연결
            Board.Image image = new Board.Image(fileName, imagePath);
            user.setProfileImage(image);

            // User 엔티티를 저장합니다
            userRepository.save(user);

            response.put("fileName", fileName);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            response.put("error", "File upload failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (RuntimeException e) {
            e.printStackTrace();
            response.put("error", "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }








}



