package com.sparta.spring_lv4.controller;

import com.sparta.spring_lv4.dto.PostRequestDto;
import com.sparta.spring_lv4.dto.PostResponseDto;
import com.sparta.spring_lv4.dto.StatusResponseDto;
import com.sparta.spring_lv4.entity.Post;
import com.sparta.spring_lv4.security.UserDetailsImpl;
import com.sparta.spring_lv4.service.PostServiceImpl;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostServiceImpl postService;

    @PostMapping("/post")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PostResponseDto createPost = postService.createPost(requestDto, userDetails.getUser());

        return ResponseEntity.ok().body(createPost);
    }

    @GetMapping("/post")
    public List<PostResponseDto> getPost() {

        return postService.getPost();
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<PostResponseDto>  getOnePost(@PathVariable Long id) {

        PostResponseDto getOnePost = postService.getOnePost(id);

        return ResponseEntity.ok().body(getOnePost);
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Post post = postService.findPost(id);
        PostResponseDto updatePost = postService.updatePost(post, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(updatePost);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<StatusResponseDto> deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {

        Post post = postService.findPost(id);
        postService.deletePost(userDetails.getUser(), post);
        return ResponseEntity.ok().body(new StatusResponseDto("삭제 되었습니다.", HttpStatus.OK.value()));
    }

    /*게시글 좋아요*/
    @PostMapping("/post/{postId}/like")
    public ResponseEntity<StatusResponseDto> postLike(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.postLike(postId, userDetails.getUser());
        return ResponseEntity.ok().body(new StatusResponseDto("좋아요 되었습니다.", HttpStatus.OK.value()));
    }

    /*게시글 좋아요 취소*/
    @DeleteMapping("/post/{postId}/like")
    public ResponseEntity<StatusResponseDto> cancelPostLike(@PathVariable Long postId,  @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.cancelPostLike(postId, userDetails.getUser());
        return ResponseEntity.ok().body(new StatusResponseDto("좋아요 취소 되었습니다.", HttpStatus.OK.value()));
    }

    /* QueryDSL 사용하여 게시글 제목 중 keyword 검색 */
    @GetMapping("/post/search")
    public List<PostResponseDto> searchPost(@Nullable @RequestParam("keyword") String keyword) {
        return postService.searchPost(keyword);
    }
}