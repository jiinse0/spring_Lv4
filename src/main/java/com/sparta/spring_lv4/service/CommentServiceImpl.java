package com.sparta.spring_lv4.service;

import com.sparta.spring_lv4.dto.CommentRequestDto;
import com.sparta.spring_lv4.dto.CommentResponseDto;
import com.sparta.spring_lv4.entity.Comment;
import com.sparta.spring_lv4.entity.CommentLike;
import com.sparta.spring_lv4.entity.Post;
import com.sparta.spring_lv4.entity.User;
import com.sparta.spring_lv4.exception.LikeException;
import com.sparta.spring_lv4.exception.NotFoundException;
import com.sparta.spring_lv4.repository.CommentLikeRepository;
import com.sparta.spring_lv4.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final PostServiceImpl PostServiceImpl;
    private final MessageSource messageSource;

    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
        Post post = PostServiceImpl.findPost(postId);
        Comment comment = new Comment(requestDto, user, post);

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Comment comment, CommentRequestDto requestDto, User user) {
        findComment(comment.getId());
        comment.updateComment(requestDto);
        return new CommentResponseDto(comment);
    }

    public void deleteComment(Comment comment, User user) {
        findComment(comment.getId());
        commentRepository.delete(comment);
    }

    // 댓글 좋아요
    @Transactional
    public void commentLike(Long commentId, User user) {
        Comment comment = findComment(commentId);

        if (user.getUsername().equals(comment.getUsername())) {
            throw new LikeException(messageSource.getMessage(
                    "own.like",
                    null,
                    "Own Commnet Like",
                    Locale.getDefault()
            ));
        } else {
            if (commentLikeRepository.findByUserAndComment(user, comment).isPresent()) {
                throw new LikeException(messageSource.getMessage(
                        "already.liked",
                        null,
                        "Already Liked",
                        Locale.getDefault()
                ));
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
            throw new LikeException(messageSource.getMessage(
                    "like.cancel.fail",
                    null,
                    "Like Cancel Fail",
                    Locale.getDefault()
            ));
        } else {
            commentLikeRepository.delete(commentLike.get());
        }
    }

    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new NotFoundException(messageSource.getMessage(
                        "not.found.comment",
                        null,
                        "Not Found Comment",
                        Locale.getDefault()
                ))
        );
    }
}
