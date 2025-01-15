package com.easystock.backend.application.service.auth;

import com.easystock.backend.presentation.api.dto.request.CreateMemberRequest;
import com.easystock.backend.presentation.api.dto.request.LoginMemberRequest;
import com.easystock.backend.presentation.api.dto.response.CreateMemberResponse;
import com.easystock.backend.presentation.api.dto.response.GetMemberProfileResponse;
import com.easystock.backend.presentation.api.dto.response.LoginMemberResponse;

public interface AuthService {
    CreateMemberResponse createMember(CreateMemberRequest request);
    LoginMemberResponse loginMember(LoginMemberRequest request);
}
