package com.sparta.spring_lv4.repository;

import com.sparta.spring_lv4.entity.Comment;
import com.sparta.spring_lv4.entity.CommentLike;
import com.sparta.spring_lv4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    Optional<CommentLike> findByUserAndComment(User user, Comment comment);
}
