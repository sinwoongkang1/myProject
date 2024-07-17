package com.example.myproject.controller;

import com.example.myproject.domain.Board;
import com.example.myproject.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/BBelog")
@RequiredArgsConstructor
public class FollowController {
    private final FollowService followService;


    @GetMapping("/following")
    public String getFollowedUserBoards(Model model,
                                        @CookieValue(value = "username", defaultValue = "") String loggedInUsername) {

        if (!loggedInUsername.isEmpty()) {
            List<Board> boards = followService.getFollowedUserBoards(loggedInUsername);
            model.addAttribute("boards", boards);
            return "board/following";
        } else {
            return "board/following";
        }
    }

    @PostMapping("/following/{username}/{id}")
    public String followUser(@PathVariable("username") String username,
                             @PathVariable("id") Long boardId,
                             @CookieValue(value = "username", defaultValue = "") String loggedInUsername) {
        if (!loggedInUsername.isEmpty()) {
            boolean isFollowing = followService.isUserFollowingAuthor(boardId, loggedInUsername);

            if (!isFollowing) {
                followService.followAuthor(username, loggedInUsername);
            } else {
                followService.deleteFollower(boardId, loggedInUsername);
            }
        }
        return "redirect:/BBelog/following";
    }
}



