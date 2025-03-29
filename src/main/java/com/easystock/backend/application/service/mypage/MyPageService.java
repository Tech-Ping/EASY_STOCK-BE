package com.easystock.backend.application.service.mypage;

import com.easystock.backend.presentation.api.dto.response.GetMemberProfileResponse;
import com.easystock.backend.presentation.api.dto.response.MonthlyStockInfoResponse;

import java.util.List;

public interface MyPageService {
    GetMemberProfileResponse getMyProfile(Long memberId);
    List<MonthlyStockInfoResponse> getMyCurrentStockStatus(Long memberId);
    List<MonthlyStockInfoResponse> getMyBookmarkTickersCurrentStatus(Long memberId);
}
