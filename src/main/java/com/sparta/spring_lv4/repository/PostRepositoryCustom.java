package com.sparta.spring_lv4.repository;

import com.sparta.spring_lv4.entity.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> getSearchPostList(String keyword);
}
