package com.example.myproject.controller;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.User;
import com.example.myproject.dto.FolloweeIdDTO;
import com.example.myproject.service.BoardService;
import com.example.myproject.service.FollowService;
import com.example.myproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/BBelog")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;
    private final UserService userService;
    private final BoardService boardService;


    @GetMapping("/following")
    public String following(@CookieValue(value = "username", defaultValue = "") String loggedInUsername,
                            Model model) {

        User user = userService.findUserByUsername(loggedInUsername);
        if (user == null) {
            return "redirect:/login";
        }

        List<FolloweeIdDTO> followUserIds = followService.findFolloweeIdsByFollowerId(user.getId());
        List<Board> boards = boardService.findBoardsByUserIds(followUserIds);

        model.addAttribute("Boards", boards);
        return "board/following";
    }



    @PostMapping("/following/{username}/{id}")
    public String follow(@CookieValue(value = "username", defaultValue = "") String loggedInUsername,
                         @PathVariable String username, @PathVariable Long id){

        Long userId = userService.findUserByUsername(loggedInUsername).getId();
        User user = userService.findUserByBoardId(id);
        Long followUser = user.getId();
        followService.followUser(userId, followUser);

        return "redirect:/BBelog/following";
    }

}



