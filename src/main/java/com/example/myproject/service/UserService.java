package com.example.myproject.service;

import com.example.myproject.domain.User;
import com.example.myproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

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

}
