package com.example.myproject.controller;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.User;
import com.example.myproject.service.BoardService;
import com.example.myproject.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/BBelog")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;

    @GetMapping("/write")
    public String write() {
        return "write";
    }

    @PostMapping("/write")
    public String write(@ModelAttribute Board board, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String username = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    username = cookie.getValue();
                    break;
                }
            }
        }
        if (username != null) {
            User user = userService.findUserByUserName(username);
            if (user != null) {
                board.setUser(user);
                boardService.save(board);
                return "redirect:/BBelog/profile";
            } else {
                return "redirect:/BBelog/login";
            }
        } else {
            return "redirect:/BBelog/login";
        }
    }
}
