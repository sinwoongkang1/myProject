package com.example.myproject.service;

import com.example.myproject.domain.User;
import com.example.myproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(new User());
    }
    public User findUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }
    public void saveUser(User user) {
         userRepository.save(user);
    }
}
