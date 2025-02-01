package com.easystock.backend.presentation.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class BookmarkResponse {
    @Schema(description = "대상 종목 이름")
    private String stockName;
    @Schema(description = "대상 종목 코드")
    private String stockCode;
    @Schema(description = "북마크 등록/해제 여부")
    private boolean isBookmarked;
    @Schema(description = "북마크 등록/해제 메시지")
    private String message;
}
