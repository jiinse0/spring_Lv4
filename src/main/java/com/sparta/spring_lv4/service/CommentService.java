package com.sparta.spring_lv4.service;

import com.sparta.spring_lv4.dto.CommentRequestDto;
import com.sparta.spring_lv4.dto.CommentResponseDto;
import com.sparta.spring_lv4.entity.*;
import com.sparta.spring_lv4.repository.CommentLikeRepository;
import com.sparta.spring_lv4.repository.CommentRepository;
import com.sparta.spring_lv4.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostService postService;

    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
        Post post = postService.findPost(postId);
        Comment comment = new Comment(requestDto, user, post);

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Comment comment, CommentRequestDto requestDto, User user) {
        comment.updateComment(requestDto);
        return new CommentResponseDto(comment);
    }

    public void deleteComment(Comment comment, User user) {
        commentRepository.delete(comment);
    }

    // 댓글 좋아요
    @Transactional
    public void commentLike(Long commentId, User user) {

        Comment comment = findComment(commentId);

        if (user.getUsername().equals(comment.getUsername())) {

            throw new IllegalArgumentException("자신의 댓글에는 좋아요를 누를 수 없습니다.");

        } else {
            if (commentLikeRepository.findByUserAndComment(user, comment).isPresent()) {
                throw new IllegalArgumentException("이미 좋아요 된 댓글입니다.");
            } else {
                CommentLike commentLike = new CommentLike(user, comment);
                commentLikeRepository.save(commentLike);
            }
        }
    }

    // 댓글 좋아요 취소
    @Transactional
    public void cancelCommentLike(Long commentId, User user) {
        Comment comment = findComment(commentId);

        Optional<CommentLike> commentLike = commentLikeRepository.findByUserAndComment(user, comment);

        if (commentLike.isEmpty()) {
            throw new IllegalArgumentException("이미 좋아요 취소 된 댓글입니다.");
        } else {
            commentLikeRepository.delete(commentLike.get());
        }
    }

    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );
    }
}
