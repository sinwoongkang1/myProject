package com.example.myproject.repository;

import com.example.myproject.domain.Board;
import com.example.myproject.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {
}
