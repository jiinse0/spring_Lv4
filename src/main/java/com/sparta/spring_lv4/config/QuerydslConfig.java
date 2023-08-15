package com.sparta.spring_lv4.config;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/*
* Spring Framework와 Querydsl 을 사용하여 JPA (Java Persistemce API) 를 통해 데이터 베이스와 상호작용하기 위한 설정 파일
* Querydsl : 타입 안전한 쿼리를 작성할 수 있도록 도와주는 라이브러리
*/
@Configuration
public class QuerydslConfig {

    /*
    * @PersistenceContext : JPA에서 사용되는 어노테이션
    *                       EntityManager를 주입받기 위해 사용된다.
    * EntityManager : JPA 에서 엔티티의 영속성(영구 저장)을 관리하고, 데이터베이스와의 상호작용을 수행하는 역할
    */
    @PersistenceContext
    private EntityManager entityManager;

    /*
     * jpaQueryFactory() : JPAQueryFactory를 생성하고, EntityManager를 생성 인자로 넘겨주어 JPAQueryFactory 객체를 생성한다.
     * jpaQueryFactory는 Querydsl 라이브러리에서 제공되며, EntityManager를 사용하여 쿼리를 실행하고 반환하는 역할을 수행한다.
     */
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}
