package com.sparta.spring_lv4.service;

import com.sparta.spring_lv4.dto.PostRequestDto;
import com.sparta.spring_lv4.dto.PostResponseDto;
import com.sparta.spring_lv4.entity.Post;
import com.sparta.spring_lv4.entity.User;

import java.util.List;


public interface PostService {

    /**
     * 게시글 생성
     * @param requestDto
     * @param user
     * @return
     */
    PostResponseDto createPost(PostRequestDto requestDto, User user);

    /**
     * 게시글 조회
     * @return
     */
    List<PostResponseDto> getPost();

    /**
     * 선택 게시글 조회
     * @param id
     * @return
     */
    PostResponseDto getOnePost(Long id);

    /**
     * 게시글 keyword 검색 (QueryDSL)
     * @param keyword
     * @return
     */
    List<PostResponseDto> searchPost(String keyword);

    /**
     * 게시글 수정
     * @param post
     * @param requestDto
     * @param user
     * @return
     */
    PostResponseDto updatePost(Post post, PostRequestDto requestDto, User user);

    /**
     * 게시글 삭제
     * @param user
     * @param post
     */
    void deletePost(User user, Post post);

    /**
     * 게시글 좋아요
     * @param postId
     * @param user
     */
    void postLike(Long postId, User user);

    /**
     * 게시글 좋아요 취소
     * @param postId
     * @param user
     */
    void cancelPostLike(Long postId, User user);

}
