package com.easystock.backend.application.service.auth;

import com.easystock.backend.presentation.api.dto.response.GetMemberProfileResponse;

public interface MyPageService {
    GetMemberProfileResponse getMyProfile(Long memberId);
}
