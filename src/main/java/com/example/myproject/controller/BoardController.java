package com.example.myproject.controller;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.User;
import com.example.myproject.service.BoardService;
import com.example.myproject.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    @PostMapping("/saveTemporary")
    public String saveTemporary(@RequestBody Board board, HttpServletRequest request) {
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
                boardService.saveTemporary(board, username);
                return "redirect:/board/temporary";
            } else {
                return "redirect:/BBelog/login";
            }
        } else {
            return "redirect:/BBelog/login";
        }
    }


    @GetMapping("/temporary")
    public String getTemporaryBoards(Model model,@CookieValue(value = "username", defaultValue = "") String username) {
        if (username.isEmpty()) {
            return "redirect:/BBelog/login";
        }
        User user = userService.findUserByUserName(username);
        List<Board> temporaryBoards = boardService.findByBoardUserAndTemporaryTrue(user);
        model.addAttribute("user", user);
        model.addAttribute("boards", temporaryBoards);
        return "temporary";
    }
}

