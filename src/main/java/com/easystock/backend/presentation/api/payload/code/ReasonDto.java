package com.easystock.backend.presentation.api.payload.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
@AllArgsConstructor
public class ReasonDto {
    private final String code;
    private final String message;
    private final Boolean isSuccess;
}
