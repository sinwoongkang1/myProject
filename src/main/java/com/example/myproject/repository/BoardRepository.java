package com.example.myproject.repository;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findByTitle(String title);
    Board save(Board board);
    void deleteByTitle(String title);
    List<Board> findBoardByUser(User user);
    List<Board> findAll();
    List<Board> findByTemporaryFalse();
    List<Board> findByTemporaryTrue();
    List<Board> findBoardByUserAndTemporaryFalse(User user);
    List<Board> findBoardByUserAndTemporaryTrue(User user);
}
