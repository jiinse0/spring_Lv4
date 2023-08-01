package com.sparta.spring_lv4.service;

import com.sparta.spring_lv4.dto.CommentRequestDto;
import com.sparta.spring_lv4.dto.CommentResponseDto;
import com.sparta.spring_lv4.entity.Comment;
import com.sparta.spring_lv4.entity.User;


public interface CommentService {

    /**
     * 댓글 생성
     * @param postId
     * @param requestDto
     * @param user
     * @return
     */
    CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user);

    /**
     * 댓글 수정
     * @param comment
     * @param requestDto
     * @param user
     * @return
     */
    CommentResponseDto updateComment(Comment comment, CommentRequestDto requestDto, User user);

    /**
     * 댓글 삭제
     * @param comment
     * @param user
     */
    void deleteComment(Comment comment, User user);

    /**
     * 댓글 좋아요
     * @param commentId
     * @param user
     */
    void commentLike(Long commentId, User user);

    /**
     * 댓글 좋아요 취소
     * @param commentId
     * @param user
     */
    void cancelCommentLike(Long commentId, User user);
}
