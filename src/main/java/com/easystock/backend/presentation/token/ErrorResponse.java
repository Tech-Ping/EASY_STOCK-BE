package com.easystock.backend.presentation.token;

import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;

public record ErrorResponse(String code, String message, int status) {
    public static ErrorResponse from(ErrorStatus status) {
        return new ErrorResponse(status.getCode(), status.getMessage(), HttpStatus.UNAUTHORIZED.value());
    }
}
