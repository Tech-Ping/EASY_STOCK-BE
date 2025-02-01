package com.easystock.backend.presentation.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BookmarkRequest {
    @NotNull(message = "stock ID는 공백일 수 없습니다.")
    private Long stockId;
}
