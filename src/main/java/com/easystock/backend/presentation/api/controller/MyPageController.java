package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.fcm.MemberDeviceService;
import com.easystock.backend.application.service.mypage.MyPageService;
import com.easystock.backend.presentation.api.dto.request.AddFcmTokenRequest;
import com.easystock.backend.presentation.api.dto.response.GetMemberProfileResponse;
import com.easystock.backend.presentation.api.dto.response.MonthlyReportResponse;
import com.easystock.backend.presentation.api.dto.response.MonthlyStockInfoResponse;
import com.easystock.backend.presentation.api.payload.ApiResponse;
import com.easystock.backend.presentation.token.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my")
@Tag(name = "마이페이지 API - /api/my ")
public class MyPageController {

    private final MyPageService myPageService;
    private final MemberDeviceService memberDeviceService;

    @GetMapping("/profile")
    @Operation(
            summary = "회원 프로필 조회 API - 회원이 본인의 프로필을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<GetMemberProfileResponse> getMyProfile(
            @Parameter(hidden = true)
            @AuthUser Long memberId
    ){
        return ApiResponse.onSuccess(myPageService.getMyProfile(memberId));
    }

    @GetMapping("/monthly-report")
    @Operation(
            summary = "매월 투자 리포트 조회 API - 회원의 지난 달 투자 리포트를 조회합니다.",
            description = """
                     \s
                     3월달에 조회 시 2월달의 회원 투자 경향 수집 데이터가 보여집니다. \s
                    \s"
                """,
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<MonthlyReportResponse> getMyMonthlyTradeReport(
            @Parameter(hidden = true)
            @AuthUser Long memberId
    ){
        return ApiResponse.onSuccess(myPageService.getMyMonthlyTradeReport(memberId, YearMonth.now()));
    }

    @GetMapping("/status/stocks")
    @Operation(
            summary = "내가 투자한 주식 현황 조회 API - 회원이 마이페이지에서 내가 투자한 주식 현황을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<List<MonthlyStockInfoResponse>> getMyStockCurrentStatus(
            @Parameter(hidden = true)
            @AuthUser Long memberId
    ){
        return ApiResponse.onSuccess(myPageService.getMyCurrentStockStatus(memberId));
    }

    @GetMapping("/status/bookmarked")
    @Operation(
            summary = "내가 북마크한 주식 현황 조회 API - 회원이 마이페이지에서 내가 투자한 주식 현황을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<List<MonthlyStockInfoResponse>> getMyBookmarkTickersCurrentStatus(
            @Parameter(hidden = true)
            @AuthUser Long memberId
    ){
        return ApiResponse.onSuccess(myPageService.getMyBookmarkTickersCurrentStatus(memberId));
    }

    @PostMapping("/fcm")
    @Operation(
            summary = "FCM 토큰 등록",
            description = "회원이 FCM 토큰을 등록합니다."
    )
    public ApiResponse<Boolean> registerFcmToken(
            @Valid
            @RequestBody AddFcmTokenRequest request,

            @Parameter(hidden = true)
            @AuthUser Long memberId
    ){
        return ApiResponse.onSuccess(memberDeviceService.addDevice(memberId, request.fcmToken()));
    }

    @DeleteMapping("/fcm")
    @Operation(
            summary =  "FCM 토큰 삭제",
            description = "회원이 등록되어있던 FCM 토큰을 삭제합니다."
    )
    public ApiResponse<Boolean> deleteFcmToken(
            String fcmToken,

            @Parameter(hidden = true)
            @AuthUser Long memberId
    ){
        return ApiResponse.onSuccess(memberDeviceService.removeDevice(memberId, fcmToken));
    }

}
