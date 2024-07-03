package com.example.myproject.controller;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.User;
import com.example.myproject.service.BoardService;
import com.example.myproject.service.LikeService;
import com.example.myproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/BBelog")
@RequiredArgsConstructor
public class LikeController {
    private final UserService userService;
    private final BoardService boardService;
    private final LikeService likeService;


    @PostMapping("/profile/{username}/{id}")
    public String likeBoard(@CookieValue(value = "username", defaultValue = "") String loggedInUsername,
                            @PathVariable String username,
                            @PathVariable Long id) {

        if (loggedInUsername.isEmpty()) {
            return "redirect:/BBelog/login";
        }

        User user = userService.findUserByUsername(username);
        Optional<Board> board = boardService.findById(id);

        if (user != null && board.isPresent()) {
            likeService.likeBoard(user, board.get());

            return "redirect:/BBelog/profile/" + username + "/" + id;
        }
        return "redirect:/BBelog/profile/" + username + "/" + id;
    }

}
