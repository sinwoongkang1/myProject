package com.example.myproject.repository;

import com.example.myproject.domain.Following;
import com.example.myproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FollowRepository extends JpaRepository<Following,Long> {

    @Query("SELECT f.followee.id FROM Following f WHERE f.follower.id = :followerId")
    List<Long> findFollowedUserIdsByFollowerId(Long followerId);

    @Modifying
    @Query("DELETE FROM Following f WHERE f.followee.id = :followeeId AND f.follower.id = :followerId")
    void deleteFolloweeByFollowerId(@Param("followeeId") Long followeeId, @Param("followerId") Long followerId);

    void deleteByFollower(User user);
    void deleteByFollowee(User user);

   boolean existsByFollowerIdAndFolloweeId (Long followerId, Long followeeId);
}
