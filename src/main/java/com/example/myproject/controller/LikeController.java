package com.example.myproject.controller;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.User;
import com.example.myproject.service.BoardService;
import com.example.myproject.service.LikeService;
import com.example.myproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        User user2 = userService.findUserByUsername(loggedInUsername);
        User user = userService.findUserByUsername(username);
        Board board = boardService.findById(id);

        List<Long> likedUsers = likeService.getUserIdsByBoardId(id);

        int userCount = user.getLiked();
        int boardCount = board.getLiked();

        if (!likedUsers.contains(user2.getId())) {
            user.setLiked(++userCount);
            board.setLiked(++boardCount);
            userService.saveUser(user);
            boardService.save(board);
            likeService.likeBoard(user2, board);
        } else {
            user.setLiked(--userCount);
            board.setLiked(--boardCount);
            userService.saveUser(user);
            boardService.save(board);
            likeService.unlikeBoard(user2, board);
        }
        return "redirect:/BBelog/profile/" + username + "/" + id;
    }


}
