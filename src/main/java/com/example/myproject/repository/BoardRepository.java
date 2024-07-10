package com.example.myproject.repository;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

//    Board findById(Long id);
    Board findByTitle(String title);
    Board save(Board board);
    void deleteByTitle(String title);
    List<Board> findBoardByUser(User user);
    List<Board> findAll();
    List<Board> findByTemporaryFalse();
    List<Board> findByTemporaryTrue();
    List<Board> findBoardByUserAndTemporaryFalse(User user);
    List<Board> findBoardByUserAndTemporaryTrue(User user);
    List<Board> findBoardByUserId(Long id);

    @Query("SELECT b FROM Board b WHERE b.user.username = :username AND b.id = :id AND b.temporary = false")
    Board findByUsernameAndBoardIdAndTemporaryFalse(@Param("username") String username, @Param("id") Long id);

    @Query("SELECT b FROM Board b WHERE b.user.username = :username AND b.id = :id AND b.temporary = true")
    Board findByUsernameAndBoardIdAndTemporaryTrue(@Param("username") String username, @Param("id") Long id);



    @Modifying
    @Transactional
    @Query("DELETE FROM Board b WHERE b.user.username = :username")
    void deleteBoardByUsername(@Param("username") String username);



    @Query("SELECT b.user.id FROM Board b WHERE b.id = :boardId")
    Long findAuthorIdByBoardId(Long boardId);

    List<Board> findByUserIdIn(List<Long> userIds);
}
