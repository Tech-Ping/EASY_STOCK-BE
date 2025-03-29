package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.FCM.MemberDeviceService;
import com.easystock.backend.application.service.mypage.MyPageService;
import com.easystock.backend.presentation.api.dto.request.AddFcmTokenRequest;
import com.easystock.backend.presentation.api.dto.response.GetMemberProfileResponse;
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
            @AuthUser Long memberId){
        return ApiResponse.onSuccess(myPageService.getMyProfile(memberId));
    }

    @GetMapping("/status/trade-stocks")
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
