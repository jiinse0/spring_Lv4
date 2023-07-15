package com.sparta.spring_lv4.controller;

import com.sparta.spring_lv4.dto.AuthRequestDto;
import com.sparta.spring_lv4.dto.StatusResponseDto;
import com.sparta.spring_lv4.jwt.JwtUtil;
import com.sparta.spring_lv4.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity<StatusResponseDto> signup(@Valid @RequestBody AuthRequestDto requestDto) {

        try {
            userService.signup(requestDto);
            return ResponseEntity.status(201).body(new StatusResponseDto("로그인이 완료되었습니다.", HttpStatus.CREATED.value()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new StatusResponseDto("이미 존재하는 회원입니다.", HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<StatusResponseDto> login(@RequestBody AuthRequestDto requestDto, HttpServletResponse response) {

        try {
            userService.login(requestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new StatusResponseDto("아이디 또는 비밀번호를 잘못 입력했습니다.", HttpStatus.BAD_REQUEST.value()));
        }

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(requestDto.getUsername(), requestDto.getRole()));

        return ResponseEntity.ok().body(new StatusResponseDto("로그인이 완료되었습니다.", HttpStatus.OK.value()));
    }

}
