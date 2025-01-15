package com.easystock.backend.presentation.token.jwt;

import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record TokenPair(
        String accessToken,
        String refreshToken
) {
    public static TokenPair of(String accessToken, String refreshToken){
        return TokenPair.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
