package com.sparta.spring_lv4.entity;

import com.sparta.spring_lv4.dto.PostRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "post")
public class Post extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column
    private String username;

    @Column(nullable = false)
    private String title;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<Comment> commentList = new ArrayList<>();

    public Post(PostRequestDto requestDto, User user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.username = user.getUsername();
        this.user = user;
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
    }
}

