package com.example.myproject.repository;

import com.example.myproject.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    User save(User user);
    Optional<User> findById(Long id);
    User findByBoards_Id(Long boardId);

}
