package com.sparta.spring_lv4.dto;

import com.sparta.spring_lv4.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String username;
    private String title;
    private String content;
    private Integer postLikeCnt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> commentList = new ArrayList<>();
//    private List<PostLike>

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.username = post.getUser().getUsername();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.postLikeCnt = post.getPostLikeList().size();
        this.createdAt = post.getCreateAt();
        this.modifiedAt = post.getModifiedAt();

        this.commentList.addAll(post.getCommentList()
                .stream()
                .map(CommentResponseDto::new)
                .sorted(Comparator.comparing(CommentResponseDto::getCreatedAt).reversed())
                .toList());
    }
}
