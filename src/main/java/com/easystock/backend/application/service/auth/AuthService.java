package com.easystock.backend.application.service.auth;

import com.easystock.backend.presentation.api.dto.request.CreateMemberRequest;
import com.easystock.backend.presentation.api.dto.response.CreateMemberResponse;

public interface AuthService {
    CreateMemberResponse createMember(CreateMemberRequest request);
}
