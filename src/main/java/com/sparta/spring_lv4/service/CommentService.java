package com.sparta.spring_lv4.service;

import com.sparta.spring_lv4.dto.CommentRequestDto;
import com.sparta.spring_lv4.dto.CommentResponseDto;
import com.sparta.spring_lv4.entity.User;
import com.sparta.spring_lv4.entity.Comment;
import com.sparta.spring_lv4.entity.Post;
import com.sparta.spring_lv4.entity.UserRoleEnum;
import com.sparta.spring_lv4.repository.CommentRepository;
import com.sparta.spring_lv4.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponseDto createComment(Long postId, CommentRequestDto requestDto, User user) {
        Post post = findByPost(postId);
        Comment comment = new Comment(requestDto, user, post);

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto requestDto, User user) {
        Comment comment = findByComment(commentId);

        if (!(user.getRole().equals(UserRoleEnum.ADMIN) || comment.getUsername().equals(user.getUsername()))) {
            throw new IllegalArgumentException("수정할 권한이 없습니다.");
        }

        comment.updateComment(requestDto);

        return new CommentResponseDto(comment);
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = findByComment(commentId);

        if (!(user.getRole().equals(UserRoleEnum.ADMIN) || comment.getUsername().equals(user.getUsername()))) {
            throw new IllegalArgumentException("삭제할 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }

    private Post findByPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
    }

    private Comment findByComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재하지 않습니다.")
        );
    }
}
