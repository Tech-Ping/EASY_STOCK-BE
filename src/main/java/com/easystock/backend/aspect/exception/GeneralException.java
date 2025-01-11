package com.easystock.backend.aspect.exception;

import com.easystock.backend.aspect.payload.code.BaseErrorCode;
import com.easystock.backend.aspect.payload.code.ErrorReasonDto;
import com.easystock.backend.aspect.payload.code.status.ErrorStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter @RequiredArgsConstructor
public class GeneralException extends RuntimeException{
    private final ErrorStatus errorStatus;
}
