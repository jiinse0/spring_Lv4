package com.sparta.spring_lv4.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.spring_lv4.entity.Post;
import com.sparta.spring_lv4.entity.QPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
* QueryDSL 관련된 클래스
* 규칙
* 1. @Repository 어노테이션이 달려 있어야 한다.
* 2. JPAQueryFactory를 spring bean으로 Autowired 되어 있어야 한다.
*/
@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    /* QueryDSL 사용하여 게시글 제목 중 keyword 검색 */
    @Override
    public List<Post> getSearchPostList(String keyword) {
        QPost post = QPost.post;

        return jpaQueryFactory
                .select(post)
                .from(post)
                .where(post.title.contains(keyword))
                .fetch();
    }
}
