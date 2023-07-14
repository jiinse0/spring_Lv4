package com.sparta.spring_lv4.jwt;

import com.sparta.spring_lv4.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil { // JWT (JSON Web Token)을 생성하고 검증하는 클래스

    // Header KEY 값
    public static final String AUTHORIZATION_HEADER = "Authorization";

    // 사용자 권한 키값. 사용자 권한도 토큰안에 넣어주기 때문에 그때 사용하는 키값
    public static final String AUTHORIZATION_KEY = "auth";

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey;
    private Key key;

    // JWT 서명에 사용되는 알고리즘
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    // 인스턴스 생성 및 의존성 주입이 완료된 후에 실행되어야 함
    // JwtUtil 인스턴스 생성 후 secretKey 값을 Base64 디코딩하여 key 를 초기화하는 역할
    @PostConstruct // 초기화 메서드를 나타내는 어노테이션으로, 객체 생성 후 한 번 실행된다. (자동 실행)
    public void init() {
        // Base64로 인코딩된 시크릿 키를 디코딩하여 바이트 배열로 변환
        byte[] bytes = Base64.getDecoder().decode(secretKey);

        // 바이트 배열을 사용하여 Key 객체를 생성
        key = Keys.hmacShaKeyFor(bytes);
    }

    // header 토큰을 가져오기 Keys.hmacShaKeyFor(bytes);
    public String resolveToken(HttpServletRequest request) { // 요청정보를 넘겨받아서 헤더 값에서 토큰을 빼온다.
        String bearerToken= request.getHeader(AUTHORIZATION_HEADER); // 요청 헤더에서 Authorization 헤더 값을 가져온다.
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7); // Bearer 접두사를 제외한 토큰 문자열을 반환
        }
        return null;
    }

    public String createToken(String username, UserRoleEnum role) {
        Date date = new Date();

        long TOKEN_TIME = 60 * 60 * 1000L;

        return BEARER_PREFIX +
                Jwts.builder() //  JWT 토큰을 생성하기 위한 빌더 객체를 생성, Jwts : JWT 토큰을 만드는 클래스
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact(); // 실제 string 형태의 jwt 토큰을 응답
    }

    // 토큰 검증
    public boolean validateToken(String token) {
        try {

            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;

        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");

        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {

            logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {

            logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }

        return false;
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

}