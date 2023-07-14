package com.sparta.spring_lv4.dto;

import com.sparta.spring_lv4.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long postId;
    private Long commentId;
    private String username;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public CommentResponseDto (Comment comment) {
        this.postId = comment.getPost().getId();
        this.commentId = comment.getId();
        this.username = comment.getComment();
        this.comment = comment.getComment();
        this.createdAt = comment.getCreateAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
