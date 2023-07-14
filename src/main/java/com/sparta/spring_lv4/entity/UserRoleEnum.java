package com.sparta.spring_lv4.entity;

import lombok.Getter;

@Getter
public enum UserRoleEnum {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String role;

    UserRoleEnum (String role) {
        this.role = role;
    }
}
