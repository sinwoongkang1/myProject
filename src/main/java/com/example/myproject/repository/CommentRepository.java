package com.example.myproject.repository;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.Comment;
import com.example.myproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findAllByBoardId(Long boardId);
    void deleteAllByBoard(Board board);
    void deleteByUser(User user);
}
