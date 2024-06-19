package com.example.myproject.controller;

import com.example.myproject.domain.User;
import com.example.myproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/eurog")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public String eurogIndex() {
        return "eurog";
    }
    @GetMapping("/join")
    public String join() {
        return "join";
    }
    @PostMapping("/join")
    public String join(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/eurog";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpSession session, RedirectAttributes redirectAttributes) {
        User foundUser = userService.findUserByUserName(user.getUsername());

        if (foundUser != null && user.getPassword().equals(foundUser.getPassword())) {
            session.setAttribute("username", foundUser.getUsername());
            return "redirect:/eurog";
        } else {
            redirectAttributes.addAttribute("error", true);
            return "redirect:/eurog/login";
        }
    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/eurog";
    }
}
