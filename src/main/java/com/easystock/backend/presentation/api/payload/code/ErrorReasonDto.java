package com.easystock.backend.presentation.api.payload.code;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter
@Builder
@AllArgsConstructor
public class ErrorReasonDto {
    private String code;
    private String message;
    private Boolean isSuccess;
    private HttpStatus httpStatus;
}
