package com.example.myproject.repository;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.Liked;
import com.example.myproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Liked,Long> {
    @Query("SELECT u.id FROM User u JOIN Liked l ON u.id = l.user.id WHERE l.board.id = :boardId")
    List<Long> getUserIdsByBoardId(@Param("boardId") Long boardId);

    public void deleteByBoardId(Long boardId);

    Liked findByUserAndBoard(User user, Board board);
}