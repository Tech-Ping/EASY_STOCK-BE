package com.easystock.backend.presentation.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor @Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;

}
