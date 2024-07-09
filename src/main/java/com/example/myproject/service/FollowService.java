package com.example.myproject.service;

import com.example.myproject.domain.Following;
import com.example.myproject.domain.User;
import com.example.myproject.dto.FolloweeIdDTO;
import com.example.myproject.repository.FollowRepository;
import com.example.myproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;



    public void followUser(Long userId, Long followingId) {
        User follower = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다: " + userId));
        User followee = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 사용자를 찾을 수 없습니다: " + followingId));

        Following following = new Following();
        following.setFollower(follower);
        following.setFollowee(followee);
        followRepository.save(following);
    }

    public List<FolloweeIdDTO> findFolloweeIdsByFollowerId(Long userId) {
        return followRepository.findFolloweeIdsByFollowerId(userId);
    }


}



