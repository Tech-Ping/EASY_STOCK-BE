package com.easystock.backend.presentation.token.jwt;

import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class JwtProvider {
    private final JwtGenerator jwtTokenProvider;

    public Long getSubject(String token){
        JwtParser jwtParser = jwtTokenProvider.getJwtParser();
        return Long.valueOf((jwtParser.parseClaimsJws(token)
                .getBody()
                .getSubject())
        );
    }
}
