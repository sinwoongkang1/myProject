package com.example.myproject.controller;


import com.example.myproject.domain.Board;
import com.example.myproject.domain.Comment;
import com.example.myproject.domain.User;
import com.example.myproject.service.BoardService;
import com.example.myproject.service.CommentService;
import com.example.myproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/BBelog")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final BoardService boardService;
    private final UserService userService;

    @PostMapping("/comment/{username}/{id}")
    public String comment(@PathVariable("username") String username, @PathVariable("id") Long id,
                          @CookieValue(value = "username", defaultValue = "") String loggedInUsername,
                          @RequestParam(name = "commentContent") String commentContent,
                          @RequestParam(name = "boardId") Long boardId){

        User commenter = userService.findByUsername(loggedInUsername);
        Board board = boardService.findById(boardId);

        Comment comment = new Comment();
        comment.setContent(commentContent);
        comment.setUser(commenter);
        comment.setBoard(board);
        comment.setDate(new Date());
        commentService.saveComment(comment);


        return "redirect:/BBelog/profile/"+username+"/"+id;

    }



}

