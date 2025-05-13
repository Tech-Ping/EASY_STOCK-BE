package com.easystock.backend.application.service.mock;

import com.easystock.backend.presentation.api.dto.request.StokenRequest;

public interface MockService {
    void acquireStoken(Long memberId, StokenRequest stokenRequest);
}
