package com.example.myproject.controller;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.User;
import com.example.myproject.service.BoardService;
import com.example.myproject.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/BBelog")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;

    @GetMapping("/write")
    public String write() {
        return "write";
    }

    @PostMapping("/write")
    public String write(@ModelAttribute Board board, HttpServletRequest request) {
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
                boardService.save(board);
                return "redirect:/BBelog/profile";
            } else {
                return "redirect:/BBelog/login";
            }
        } else {
            return "redirect:/BBelog/login";
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
        model.addAttribute("user", user);
        model.addAttribute("boards", temporaryBoards);
        return "temporary";
    }

    @GetMapping("/profile/{username}/{id}")
    public String profile(@PathVariable String username, @PathVariable Long id,
                          @CookieValue(value = "username", defaultValue = "") String cookieUsername,
                          Model model) {
        User user = userService.findUserByUsername(username);
        Board board = boardService.findByUsernameAndBoardIdAndTemporaryFalse(username, id);
        model.addAttribute("user", user);
        model.addAttribute("id", id);
        model.addAttribute("board", board);

        if (username.equals(cookieUsername)) {
            return "board_writed_me";
        } else {
            return "board_writed";
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
        return "edit";
    }

    @PostMapping("/update/{username}/{id}")
    public String update(@PathVariable String username,
                         @PathVariable Long id,
                         @RequestParam String title,
                         @RequestParam String content,
                         Model model) {
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
        return "wirted_temporary";
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
        return "temporary_edit";
    }

    @PostMapping("/temporary/update/{username}/{id}")
    public String temporaryUpdate(@PathVariable String username,
                                  @PathVariable Long id,
                                  @RequestParam String title,
                                  @RequestParam String content,
                                  Model model) {
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
                                @PathVariable Long id,
                                @RequestParam String title,
                                @RequestParam String content,
                                Model model) {
        Board board = boardService.findByUsernameAndBoardIdAndTemporaryTrue(username, id);
            board.setTitle(title);
            board.setContent(content);
            board.setTemporary(false);
            board.setWriteTime(new Date());
            boardService.save(board);

            model.addAttribute("board",board);
            model.addAttribute("username", username);
            model.addAttribute("id", id);

        return "redirect:/profile";
    }
}