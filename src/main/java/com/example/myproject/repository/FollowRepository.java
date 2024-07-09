package com.example.myproject.repository;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.Following;
import com.example.myproject.domain.User;
import com.example.myproject.dto.FolloweeIdDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FollowRepository extends JpaRepository<Following,Long> {

    @Query("SELECT f.follower.id FROM Following f WHERE f.board.id = :boardId")
    List<Long> findUserIdsByBoardId(@Param("boardId") Long boardId);


    @Query("SELECT new com.example.myproject.dto.FolloweeIdDTO(f.followee.id) FROM Following f WHERE f.follower.id = :userId")
    List<FolloweeIdDTO> findFolloweeIdsByFollowerId(@Param("userId") Long userId);


}
