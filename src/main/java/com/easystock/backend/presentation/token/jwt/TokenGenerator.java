package com.easystock.backend.presentation.token.jwt;

public interface TokenGenerator {
    Token extractTokenData(String token);
    String generateAccessToken(Long memberId);
    String generateRefreshToken(Long memberId);
}
