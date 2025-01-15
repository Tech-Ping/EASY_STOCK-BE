package com.easystock.backend.aspect.exception;
import com.easystock.backend.presentation.api.payload.code.BaseErrorCode;
import com.easystock.backend.presentation.api.payload.code.ErrorReasonDto;
import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public class GeneralException extends RuntimeException{
    private final ErrorStatus errorStatus;
    private BaseErrorCode code;

    public ErrorReasonDto getErrorReason(){
        return this.code.getReason();
    }

    public ErrorReasonDto getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
