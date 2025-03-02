package com.easystock.backend.presentation.token.jwt;

import com.easystock.backend.aspect.exception.AuthException;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor
public class JwtProvider {
    private final JwtGenerator jwtTokenProvider;

    public Long getSubject(String token){
        JwtParser jwtParser = jwtTokenProvider.getJwtParser();
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        String subject = claims.getSubject();
        if(subject == null) throw new AuthException(ErrorStatus.AUTH_UNKNOWN_MEMBER_ID);
        return Long.valueOf(subject);
    }

    public Long getMemberIdFromRefreshToken(String refreshToken) {
        JwtParser jwtParser = jwtTokenProvider.getJwtParser();
        String memberId = jwtParser.parseClaimsJws(refreshToken)
                .getBody()
                .getSubject();
        return Long.valueOf(memberId);
    }
}
