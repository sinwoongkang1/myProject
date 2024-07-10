package com.example.myproject.service;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.Following;
import com.example.myproject.domain.User;
import com.example.myproject.repository.BoardRepository;
import com.example.myproject.repository.FollowRepository;
import com.example.myproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;


    public void followAuthor(String writerName, String currentUsername) {
        User author = userRepository.findByUsername(writerName);
        User currentUser = userRepository.findByUsername(currentUsername);

        Following following = new Following();
        following.setFollower(currentUser);
        following.setFollowee(author);

        followRepository.save(following);
    }

    public List<Board> getFollowedUserBoards(String currentUsername) {
        User currentUser = userRepository.findByUsername(currentUsername);
        List<Long> followedUserIds = followRepository.findFollowedUserIdsByFollowerId(currentUser.getId());

        return boardRepository.findByUserIdIn(followedUserIds);
    }
    @Transactional
    public void deleteFollower(Long boardId, String currentUsername) {
        Long authorId = boardRepository.findAuthorIdByBoardId(boardId);
        Long CurrentUserId = userRepository.findByUsername(currentUsername).getId();
        followRepository.deleteFolloweeByFollowerId(authorId,CurrentUserId);
    }
    public boolean isUserFollowingAuthor(Long boardId, String currentUsername) {
        Long authorId = boardRepository.findAuthorIdByBoardId(boardId);
        Long currentUserId = userRepository.findByUsername(currentUsername).getId();
        return followRepository.existsByFollowerIdAndFolloweeId(currentUserId, authorId);
    }
}



