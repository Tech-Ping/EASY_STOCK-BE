package com.easystock.backend.application.service.token;


import com.easystock.backend.presentation.api.dto.response.TokenResponse;
import org.springframework.stereotype.Service;

public interface TokenService {
    TokenResponse createToken(Long memberId);
}
