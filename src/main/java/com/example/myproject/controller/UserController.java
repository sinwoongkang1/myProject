package com.example.myproject.controller;

import com.example.myproject.domain.Admin;
import com.example.myproject.domain.User;
import com.example.myproject.service.AdminService;
import com.example.myproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;


@Controller
@RequestMapping("/BBelog")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AdminService adminService;

    @GetMapping
    public String BBelogIndex() {
        return "BBelog";
    }
    @GetMapping("/join")

    public String join() {
        return "join";
    }
    //회원가입 진행 - 같은 아이디 존재 확인, 비밀번호 동일하게 입력 확인(JS)
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
    //로그인 진행 -  비밀번호 틀릴시 팝업
    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpSession session, RedirectAttributes redirectAttributes
    ) {
        User foundUser = userService.findUserByUserName(user.getUsername());
        if (foundUser != null && user.getPassword().equals(foundUser.getPassword())) {
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
    public String profile(Model model, Principal principal, HttpSession session) {
        String username = principal.getName();
        User user = userService.findUserByUserName(username);
        if (user == null) {
            return "redirect:/profile";
        } else {
            session.setAttribute("loggedInUser", user);
            model.addAttribute("user", user);
            return "profile";
        }
    }
    }
