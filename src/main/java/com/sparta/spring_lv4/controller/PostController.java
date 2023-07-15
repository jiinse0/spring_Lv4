package com.sparta.spring_lv4.controller;

import com.sparta.spring_lv4.dto.PostRequestDto;
import com.sparta.spring_lv4.dto.PostResponseDto;
import com.sparta.spring_lv4.dto.StatusResponseDto;
import com.sparta.spring_lv4.security.UserDetailsImpl;
import com.sparta.spring_lv4.service.PostService;
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

    private final PostService postService;

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

        PostResponseDto updatePost = postService.updatePost(id, requestDto, userDetails.getUser());
        return ResponseEntity.ok().body(updatePost);
    }

    @DeleteMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long id) {

        try {
            PostResponseDto deletePost = postService.deletePost(userDetails.getUser(), id);
            return ResponseEntity.ok().body(deletePost);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}