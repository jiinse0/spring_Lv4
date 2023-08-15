package com.sparta.spring_lv4.service;

import com.sparta.spring_lv4.dto.PostRequestDto;
import com.sparta.spring_lv4.dto.PostResponseDto;
import com.sparta.spring_lv4.entity.Post;
import com.sparta.spring_lv4.entity.PostLike;
import com.sparta.spring_lv4.entity.User;
import com.sparta.spring_lv4.exception.LikeException;
import com.sparta.spring_lv4.exception.NotFoundException;
import com.sparta.spring_lv4.repository.PostLikeRepository;
import com.sparta.spring_lv4.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final MessageSource messageSource;

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
        Post post = findPost(id);
        return new PostResponseDto(post);
    }

    /* QueryDSL 사용하여 게시글 제목 중 keyword 검색 */
    @Override
    public List<PostResponseDto> searchPost(String keyword) {
        return postRepository.getSearchPostList(keyword)
                .stream()
                .map(PostResponseDto::new)
                .toList();
    }

    @Transactional
    public PostResponseDto updatePost(Post post, PostRequestDto requestDto, User user) {
        findPost(post.getId());
        post.update(requestDto);
        return new PostResponseDto(post);
    }

    public void deletePost(User user, Post post) {
        findPost(post.getId());
        postRepository.delete(post);
    }

    // 게시글 좋아요
    @Transactional
    public void postLike(Long postId, User user) {
        Post post = findPost(postId);

        if (user.getUsername().equals(post.getUsername())) {

            throw new LikeException(messageSource.getMessage(
                    "own.like",
                    null,
                    "Own Post Like",
                    Locale.getDefault()
            ));

        } else {

            if (postLikeRepository.findByUserAndPost(user, post).isPresent()) {
                throw new LikeException(messageSource.getMessage(
                        "already.liked",
                        null,
                        "Already Liked",
                        Locale.getDefault()
                ));
            } else {
                PostLike postLike = new PostLike(user, post);
                postLikeRepository.save(postLike);
            }
        }
    }

    @Transactional
    public void cancelPostLike(Long postId, User user) {
        Post post = findPost(postId);

        Optional<PostLike> postLike = postLikeRepository.findByUserAndPost(user, post);

        if (postLike.isEmpty()) {
            throw new LikeException(messageSource.getMessage(
                    "like.cancel.fail",
                    null,
                    "Like Cancel Fail",
                    Locale.getDefault()
            ));
        } else {
            postLikeRepository.delete(postLike.get());

        }
    }

    public Post findPost(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new NotFoundException(messageSource.getMessage(
                        "not.found.post",
                        null,
                        "Not Found Post",
                        Locale.getDefault()
                ))
        );
    }

}
