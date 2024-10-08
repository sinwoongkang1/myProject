package com.example.myproject.controller;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.Comment;
import com.example.myproject.domain.User;
import com.example.myproject.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/BBelog")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;
    private final CommentService commentService;
    private final LikeService likeService;

    @GetMapping("/write")
    public String write() {
        return "/board/write";
    }

    @PostMapping("/write")
    public String write(@ModelAttribute Board board, HttpServletRequest request,Model model) {
        Cookie[] cookies = request.getCookies();
        String username = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    username = cookie.getValue();
                    break;
                }
            }
        }
        if (username != null) {
            User user = userService.findUserByUsername(username);
            Board savedBoard = boardService.findBoardByUserIdAndContent(user.getId(),"temporary");
            if (user != null && savedBoard == null) {
                board.setUser(user);
                boardService.save(board);
                return "redirect:/BBelog/profile";
            } else {
                savedBoard.setUser(user);
                savedBoard.setTitle(board.getTitle());
                savedBoard.setContent(board.getContent());
                boardService.save(savedBoard);
                model.addAttribute("board", savedBoard);
                return "redirect:/BBelog/profile";
            }
        } else {
            return "redirect:/BBelog/profile";
        }
    }

    @PostMapping("/saveTemporary")
    public String saveTemporary(@RequestBody Board board, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String username = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    username = cookie.getValue();
                    break;
                }
            }
        }
        if (username != null) {
            User user = userService.findUserByUsername(username);
            if (user != null) {
                board.setUser(user);
                boardService.saveTemporary(board, username);
                return "redirect:/board/temporary";
            } else {
                return "redirect:/BBelog/login";
            }
        } else {
            return "redirect:/BBelog/login";
        }
    }

    @GetMapping("/temporary")
    public String getTemporaryBoards(Model model, @CookieValue(value = "username", defaultValue = "") String username) {
        if (username.isEmpty()) {
            return "redirect:/BBelog/login";
        }
        User user = userService.findUserByUsername(username);
        List<Board> temporaryBoards = boardService.findByBoardUserAndTemporaryTrue(user);
        temporaryBoards.sort(Comparator.comparing(Board::getWriteTime).reversed());
        model.addAttribute("user", user);
        model.addAttribute("boards", temporaryBoards);
        return "/temporary/temporary_list";
    }

    @GetMapping("/profile/{username}/{id}")
    public String profile(@PathVariable String username, @PathVariable Long id,
                          @CookieValue(value = "username", defaultValue = "") String cookieUsername,
                          Model model) {
        User user = userService.findUserByUsername(username);
        Board board = boardService.findByUsernameAndBoardIdAndTemporaryFalse(username, id);
        List<Comment> comments = commentService.findAllByBoardId(id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        model.addAttribute("board", board);
        model.addAttribute("comments", comments);
        if (username.equals(cookieUsername)) {
            return "/board/board_writed_me";
        } else {
            return "/board/board_writed";
        }
    }

    @PostMapping("/delete/{username}/{id}")
    public String deletePost(@PathVariable String username, @PathVariable Long id, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        User user = userService.findUserByUsername(username);
        String cookieUsername = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("username")) {
                    cookieUsername = cookie.getValue();
                    break;
                }
            }
        }
        if (username.equals(cookieUsername)) {
            Board board = boardService.findByUsernameAndBoardIdAndTemporaryFalse(username, id);
            if (board != null) {
                likeService.deleteAllLikedByBoard(board);
                commentService.deleteAllByBoard(board);
                boardService.deleteBoard(board);
            }
        }
        return "redirect:/BBelog";
    }

    @GetMapping("/update/{username}/{id}")
    public String updatePost(@PathVariable String username, @PathVariable Long id, Model model) {
        User user = userService.findUserByUsername(username);
        Board board = boardService.findByUsernameAndBoardIdAndTemporaryFalse(username, id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        model.addAttribute("board", board);
        return "/board/edit";
    }

    @PostMapping("/update/{username}/{id}")
    public String update(@PathVariable String username,
                         @PathVariable Long id,
                         @RequestParam String title,
                         @RequestParam String content)  {
        Board board = boardService.findByUsernameAndBoardIdAndTemporaryFalse(username, id);
        if (board != null) {
            board.setTitle(title);
            board.setContent(content);
            board.setWriteTime(new Date());
            boardService.save(board);
        }
        return "redirect:/BBelog";
    }

    @GetMapping("/temporary/{username}/{id}")
    public String temporary(@PathVariable String username, @PathVariable Long id, Model model) {
        User user = userService.findUserByUsername(username);
        Board board = boardService.findByUsernameAndBoardIdAndTemporaryTrue(username, id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        model.addAttribute("board", board);
        return "/temporary/wirted_temporary";
    }

    @PostMapping("/temporary/delete/{username}/{id}")
    public String temporaryDelete(@PathVariable String username, @PathVariable Long id, Model model) {
        User user = userService.findUserByUsername(username);
        Board board = boardService.findByUsernameAndBoardIdAndTemporaryTrue(username, id);
        if (board != null) {
            boardService.deleteBoard(board);
        }
        return "redirect:/BBelog/temporary";
    }

    @GetMapping("/temporary/update/{username}/{id}")
    public String temporaryUpdate(@PathVariable String username, @PathVariable Long id, Model model) {
        Board board = boardService.findByUsernameAndBoardIdAndTemporaryTrue(username, id);
        if (board == null) {
            return "error";
        }
        model.addAttribute("board", board);
        return "/temporary/temporary_edit";
    }

    @PostMapping("/temporary/update/{username}/{id}")
    public String temporaryUpdate(@PathVariable String username,
                                  @PathVariable Long id,
                                  @RequestParam String title,
                                  @RequestParam String content) {
        Board board = boardService.findByUsernameAndBoardIdAndTemporaryTrue(username, id);
        if (board != null) {
            board.setTitle(title);
            board.setContent(content);
            board.setWriteTime(new Date());
            boardService.save(board);
        }
        return "redirect:/BBelog/temporary";
    }

    @PostMapping("/temporary/push/{username}/{id}")
    public String temporaryPush(@PathVariable String username,
                                @PathVariable String id,
                                @RequestParam String title,
                                @RequestParam String content,
                                Model model) {
        Long userId = Long.parseLong(id);
        Board board = boardService.findByUsernameAndBoardIdAndTemporaryTrue(username, userId);
            board.setTitle(title);
            board.setContent(content);
            board.setTemporary(false);
            board.setWriteTime(new Date());
            boardService.save(board);
            model.addAttribute("board",board);
            model.addAttribute("username", username);
            model.addAttribute("id", id);
        return "redirect:/BBelog/profile";
    }

    private String AWS_ACCESS_KEY = "AHUEIEJG345ASG";
    private String AWS_SECRET_KEY = "as5dgi4hji3u43ahg+mpg";
}