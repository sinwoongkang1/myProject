package com.example.myproject.service;

import com.example.myproject.domain.User;
import com.example.myproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final LikeRepository likeRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    //가입한 user DB 에 저장하기
    public void saveUser(User user) {
        userRepository.save(user);
    }

    //DB에 있는 user 이름으로 찾기
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    //DB에 있는 user 이름과, 입력한 이름 비교해서 boolean 값 반환하기
    public boolean existsByUsername(String username) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));
        return userOptional.isPresent();
    }
public User findByUsername(String username) {
        return userRepository.findByUsername(username);
}

public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
}

@Transactional
public void deleteAllByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            followRepository.deleteByFollower(user);
            followRepository.deleteByFollowee(user);
            likeRepository.deleteByUser(user);
            commentRepository.deleteByUser(user);
            boardRepository.deleteByUser(user);
            userRepository.deleteAllByUsername(username);
        }

}
}
