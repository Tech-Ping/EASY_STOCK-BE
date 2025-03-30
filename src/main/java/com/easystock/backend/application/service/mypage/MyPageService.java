package com.easystock.backend.application.service.mypage;

import com.easystock.backend.presentation.api.dto.response.GetMemberProfileResponse;
import com.easystock.backend.presentation.api.dto.response.MonthlyReportResponse;
import com.easystock.backend.presentation.api.dto.response.MonthlyStockInfoResponse;

import java.time.YearMonth;
import java.util.List;

public interface MyPageService {
    GetMemberProfileResponse getMyProfile(Long memberId);
    MonthlyReportResponse getMyMonthlyTradeReport(Long memberId, YearMonth yearMonth);
    List<MonthlyStockInfoResponse> getMyCurrentStockStatus(Long memberId);
    List<MonthlyStockInfoResponse> getMyBookmarkTickersCurrentStatus(Long memberId);
}
