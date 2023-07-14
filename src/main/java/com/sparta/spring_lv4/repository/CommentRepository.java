package com.sparta.spring_lv4.repository;

import com.sparta.spring_lv4.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
