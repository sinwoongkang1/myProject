package com.example.myproject.controller;

import com.example.myproject.domain.Admin;
import com.example.myproject.domain.Board;
import com.example.myproject.domain.User;
import com.example.myproject.service.AdminService;
import com.example.myproject.service.BoardService;
import com.example.myproject.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/BBelog")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AdminService adminService;
    private final BoardService boardService;

    @GetMapping
    public String BBelogIndex(Model model) {
        List<Board> boards = boardService.findByTemporaryFalse();
        model.addAttribute("boards", boards);
        return "BBelog";
    }

    @GetMapping("/join")
    public String join() {
        return "join";
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
        return "login";
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
            return "redirect:/BBelog/login";
        }
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return "redirect:/BBelog";
        }
            List<Board> boards = boardService.findByBoardUserAndTemporaryFalse(user);
            model.addAttribute("user", user);
            model.addAttribute("boards", boards);
            return "profile";

    }






}



