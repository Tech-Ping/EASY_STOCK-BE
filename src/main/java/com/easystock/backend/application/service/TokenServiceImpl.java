package com.easystock.backend.application.service;

import com.easystock.backend.presentation.api.dto.response.TokenResponse;
import com.easystock.backend.presentation.token.jwt.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenGenerator tokenGenerator;

    @Override
    public TokenResponse createToken(Long memberId){
        String accessToken = tokenGenerator.generateAccessToken(memberId);
        String refreshToken = tokenGenerator.generateRefreshToken(memberId);
        return new TokenResponse(accessToken, refreshToken);
    }
}
