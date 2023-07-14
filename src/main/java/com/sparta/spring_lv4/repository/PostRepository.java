package com.sparta.spring_lv4.repository;

import com.sparta.spring_lv4.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findAllByOrderByCreateAtDesc();
}
