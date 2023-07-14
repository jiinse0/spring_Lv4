package com.sparta.spring_lv4.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.spring_lv4.jwt.JwtAuthorizationFilter;
import com.sparta.spring_lv4.jwt.JwtUtil;
import com.sparta.spring_lv4.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf((csrf) -> csrf.disable());

        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        // 중요
        // authorizeHttpRequests : Http 요청에 대한 인증과 권한 부여 설정
        http.authorizeHttpRequests((authorizeHttpRequests) -> // Http 요청에 대한 인증을 어떻게 할거냐
                authorizeHttpRequests
                        // requestMatchers() : 특정 요청 매처에 대한 접근 권한 설정을 지정
                        //                     괄호 안의 요청 정보가 조건에 맞는지 확인하고, 맞는다면 permitAll() 수행
                        // permitAll() : requestMatchers() 에 해당하는 요청은 다 승인 해 주겠다. 따로 인증 수행 하지 않겠다.
                        // StaticResources : resources 하위에 있는 application.properties
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/api/auth/**").permitAll() // '/api/auth/'로 시작하는 요청 모두 접근 허가 (회원가입, 로그인)
                        .requestMatchers(HttpMethod.GET, "/api/post/**").permitAll() // 'GET /api/posts'로 시작하는 요청 모두 접근 허가 (게시글 조회)
                        .anyRequest().authenticated() // 그 외 모든 요청 인증처리
        );

        http.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}