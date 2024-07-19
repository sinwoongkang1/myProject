package com.example.myproject.service;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.Comment;
import com.example.myproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> findAllByBoardId(Long boardId) {
        return commentRepository.findAllByBoardId(boardId);
    }

    @Transactional
    public void deleteAllByBoard(Board board) {
        commentRepository.deleteAllByBoard(board);
    }
}
