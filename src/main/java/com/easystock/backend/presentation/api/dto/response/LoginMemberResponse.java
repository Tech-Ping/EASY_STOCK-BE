package com.easystock.backend.presentation.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor @Builder
public class LoginMemberResponse {
    public TokenResponse tokenResponse;
    public Long memberId;
}
