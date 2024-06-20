package com.example.myproject.repository;

import com.example.myproject.domain.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Integer> {
    Admin save(Admin admin);
}
