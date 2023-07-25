package com.sparta.spring_lv4.service;

import com.sparta.spring_lv4.dto.PostRequestDto;
import com.sparta.spring_lv4.dto.PostResponseDto;
import com.sparta.spring_lv4.entity.Post;
import com.sparta.spring_lv4.entity.PostLike;
import com.sparta.spring_lv4.entity.User;
import com.sparta.spring_lv4.entity.UserRoleEnum;
import com.sparta.spring_lv4.repository.PostLikeRepository;
import com.sparta.spring_lv4.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    public PostResponseDto createPost(PostRequestDto requestDto, User user) {
        Post post = new Post(requestDto, user);

        postRepository.save(post);

        return new PostResponseDto(post);
    }

    public List<PostResponseDto> getPost() {
        return postRepository.findAllByOrderByCreateAtDesc()
                .stream()
                .map(PostResponseDto::new)
                .toList();
    }

    public PostResponseDto getOnePost(Long id) {
        Post post = findByPost(id);

        return new PostResponseDto(post);
    }

    @Transactional
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto, User user) {
        Post post = findByPost(id);

        if (!(post.getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN))) {
            throw new IllegalArgumentException("수정할 권한이 없습니다.");
        }

        post.update(requestDto);

        return new PostResponseDto(post);
    }

    public void deletePost(User user, Long id) {
        Post post = findByPost(id);

        if (!(post.getUsername().equals(user.getUsername()) || user.getRole().equals(UserRoleEnum.ADMIN))) {
            throw new IllegalArgumentException("삭제할 권한이 없습니다.");
        }

        postRepository.delete(post);
    }

    // 게시글 좋아요
    @Transactional
    public void postLike(Long postId, User user) {
        Post post = findByPost(postId);

        if (user.getUsername().equals(post.getUsername())) {

            throw new IllegalArgumentException("자신의 게시글에는 좋아요를 누를 수 없습니다.");

        } else {

            if (postLikeRepository.findByUserAndPost(user, post).isPresent()) {
                throw new IllegalArgumentException("이미 좋아요한 게시글 입니다.");
            } else {
                PostLike postLike = new PostLike(user, post);
                postLikeRepository.save(postLike);
            }
        }
    }

    @Transactional
    public void cancelPostLike(Long postId, User user) {
        Post post = findByPost(postId);

        Optional<PostLike> postLike = postLikeRepository.findByUserAndPost(user, post);

        if (postLike.isEmpty()) {
            throw new IllegalArgumentException("좋아요 취소 되어있는 게시물 입니다.");
        } else {
            postLikeRepository.delete(postLike.get());

        }
    }

    private Post findByPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("등록된 게시글이 없습니다.")
        );
    }

}
