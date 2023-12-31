package com.sparta.spring_lv4.controller;

import com.sparta.spring_lv4.dto.CommentRequestDto;
import com.sparta.spring_lv4.dto.CommentResponseDto;
import com.sparta.spring_lv4.dto.StatusResponseDto;
import com.sparta.spring_lv4.entity.Comment;
import com.sparta.spring_lv4.security.UserDetailsImpl;
import com.sparta.spring_lv4.service.CommentService;
import com.sparta.spring_lv4.service.CommentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {
    private final CommentServiceImpl commentService;
    private final MessageSource messageSource;

    @PostMapping("/{postId}/comment")
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long postId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CommentResponseDto createComment = commentService.createComment(postId, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(createComment);
    }

    @PutMapping("/comment/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId, @RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Comment comment = commentService.findComment(commentId);
        CommentResponseDto updateComment = commentService.updateComment(comment, requestDto, userDetails.getUser());

        return ResponseEntity.ok().body(updateComment);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<StatusResponseDto> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Comment comment = commentService.findComment(commentId);
        commentService.deleteComment(comment, userDetails.getUser());

        return ResponseEntity.ok().body(new StatusResponseDto("댓글 삭제가 완료되었습니다.", HttpStatus.OK.value()));
    }

    // 댓글 좋아요
    @PostMapping("/comment/{commentId}/like")
    public ResponseEntity<StatusResponseDto> commentLike(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.commentLike(commentId, userDetails.getUser());
        return ResponseEntity.ok().body(new StatusResponseDto("좋아요 되었습니다.", HttpStatus.OK.value()));
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/comment/{commentId}/like")
    public ResponseEntity<StatusResponseDto> cancelCommentLike(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.cancelCommentLike(commentId, userDetails.getUser());
        return ResponseEntity.ok().body(new StatusResponseDto("좋아요 취소 되었습니다.", HttpStatus.OK.value()));
    }
}
