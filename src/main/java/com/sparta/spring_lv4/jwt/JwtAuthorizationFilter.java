package com.sparta.spring_lv4.jwt;

import com.sparta.spring_lv4.dto.StatusResponseDto;
import com.sparta.spring_lv4.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 들어온 jwt 검증 및 인가 (너 jwt에 문제 없어 너 허가!! 라는 느낌)
public class JwtAuthorizationFilter extends OncePerRequestFilter { // OncePerRequestFilter : 요청 하나당 한번씩 수행하는 필터

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // 로그인 토큰없이 시도 시 로그인 안내 메서드
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtil.resolveToken(request);

        if(token != null) {
            if(!jwtUtil.validateToken(token)){ // 유효하지 않은 토큰인 경우 조건문 내부 코드 실행
                StatusResponseDto responseDto = new StatusResponseDto("토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST.value());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json; charset=UTF-8");
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(token);

            setAuthentication(info.getSubject());
        }

        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username); // 사용자 정보 로드
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // 인증객체 반환
    }
}