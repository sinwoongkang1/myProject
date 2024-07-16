package com.example.myproject.repository;

import com.example.myproject.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    Set<Comment> findAllByBoardId(Long boardId);
}
