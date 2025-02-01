package com.easystock.backend.presentation.api.controller;

import com.easystock.backend.application.service.bookmark.BookmarkService;
import com.easystock.backend.presentation.api.dto.request.BookmarkRequest;
import com.easystock.backend.presentation.api.dto.response.BookmarkResponse;
import com.easystock.backend.presentation.api.payload.ApiResponse;
import com.easystock.backend.presentation.token.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookmarks")
@Tag(name = "북마크 API - /api/bookmarks ")
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("")
    @Operation(
            summary = "회원 북마크 등록/해제 API - 회원이 주요 기업에 대한 북마크를 등록/해제합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ApiResponse<BookmarkResponse> registerOrUnregister(
            @Parameter(hidden = true)
            @AuthUser Long memberId,
            @RequestBody @Valid BookmarkRequest request){
        return ApiResponse.onSuccess(bookmarkService.registerOrUnregister(memberId, request.getStockId()));
    }
}
