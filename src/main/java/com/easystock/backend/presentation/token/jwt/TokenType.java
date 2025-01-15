package com.easystock.backend.presentation.token.jwt;

import com.easystock.backend.aspect.exception.TokenInvalidTypeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TokenType {
    ACCESS_TOKEN("access_token"),
    REFRESH_TOKEN("refresh_token");

    private final String name;

    public static TokenType fromString(String name){
        return switch (name.toUpperCase()) {
            case "ACCESS_TOKEN" -> ACCESS_TOKEN;
            case "REFRESH_TOKEN" -> REFRESH_TOKEN;
            default -> throw new TokenInvalidTypeException();
        };
    }
}
