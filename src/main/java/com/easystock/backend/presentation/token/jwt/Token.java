package com.easystock.backend.presentation.token.jwt;

public record Token (
        Long memberId,
        TokenType tokenType
){
}
