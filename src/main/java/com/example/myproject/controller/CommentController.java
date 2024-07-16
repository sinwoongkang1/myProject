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
                          @RequestParam(name = "boardId") Long boardId,
                          Model model){

        User commenter = userService.findByUsername(loggedInUsername);
        Board board = boardService.findById(boardId);
        Set<Comment> comments = commentService.findAllByBoardId(boardId);

        System.out.println("댓글 쓴 사람 ::::::::::::::::::::"+loggedInUsername);
        System.out.println("댓글 내용 ::::::::::::::::::::"+commentContent);
        System.out.println("댓글을 단 게시판 아이디 ::::::::::::::::::::"+boardId);


        Comment comment = new Comment();
        comment.setContent(commentContent);
        comment.setUser(commenter);
        comment.setBoard(board);
        comment.setDate(new Date());
        commentService.saveComment(comment);


        model.addAttribute("comments", comments);
        model.addAttribute("commenter", commenter);

        System.out.println("보내는 댓글 내 용  ::::::::::::::::::::"+comments);
        System.out.println("보내는 댓글 쓴 사람 ::::::::::::::::::::"+commenter.getUsername());


        return "redirect:/BBelog/profile/"+username+"/"+id;

    }



}

