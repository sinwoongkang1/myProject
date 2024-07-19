package com.example.myproject.controller;

import com.example.myproject.domain.Admin;
import com.example.myproject.domain.Board;
import com.example.myproject.domain.User;
import com.example.myproject.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.Comparator;
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
            return "redirect:/BBelog/login";
        }
    }

    @GetMapping("/login")
    public String login() {
        return "/main/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpSession session, RedirectAttributes redirectAttributes
            , HttpServletResponse response) {
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
        int cal = myCal - (500 * boardSize);
        user.setCal(cal);
        userService.saveUser(user);
        boards.sort(Comparator.comparing(Board::getWriteTime).reversed());
        model.addAttribute("user", user);
        model.addAttribute("boards", boards);
        model.addAttribute("cal", cal);
        return "/main/profile";
    }

    @GetMapping("/editUser")
    public String editUser(Model model, @CookieValue(value = "username", defaultValue = "") String username) {
        User user = userService.findUserByUsername(username);
        model.addAttribute("user", user);
        return "/main/editUser";
    }

    @PostMapping("/editUser")
    public String editUser(@ModelAttribute User user,
                           @CookieValue(value = "username", defaultValue = "") String username) {
        User user1 = userService.findUserByUsername(username);
        user1.setPassword(user.getPassword());
        user1.setEmail(user.getEmail());
        user1.setWantedWeight(user.getWantedWeight());
        userService.saveUser(user1);
        return "redirect:/BBelog/profile";
    }

    @PostMapping("/withdraw")
    public String withdraw(@ModelAttribute User user,
                           @CookieValue(value = "username", defaultValue = "") String username) {
        userService.deleteAllByUsername(username);
    return "redirect:/BBelog";
    }
}


