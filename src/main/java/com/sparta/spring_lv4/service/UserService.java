package com.sparta.spring_lv4.service;

import com.sparta.spring_lv4.dto.AuthRequestDto;
import com.sparta.spring_lv4.entity.UserRoleEnum;
import com.sparta.spring_lv4.entity.User;
import com.sparta.spring_lv4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(AuthRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }

        UserRoleEnum role = UserRoleEnum.USER;

        if (!requestDto.getRole().equals(role)) {
            role = UserRoleEnum.ADMIN;
        }

        User user = new User(username, password, role);

        userRepository.save(user);
    }

    public void login(AuthRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 아이디가 없습니다.")
        );

        if (passwordEncoder.matches(requestDto.getPassword(), password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");

        }
    }
}
