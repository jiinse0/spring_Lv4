package com.sparta.spring_lv4.repository;

import com.sparta.spring_lv4.entity.Post;
import com.sparta.spring_lv4.entity.PostLike;
import com.sparta.spring_lv4.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    Optional<PostLike> findByUserAndPost(User user, Post post);
}
