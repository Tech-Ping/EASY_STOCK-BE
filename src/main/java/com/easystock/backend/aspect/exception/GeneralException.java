package com.easystock.backend.aspect.exception;

import com.easystock.backend.aspect.payload.code.BaseErrorCode;
import com.easystock.backend.aspect.payload.code.ErrorReasonDto;
import com.easystock.backend.aspect.payload.code.status.ErrorStatus;
import org.springframework.http.HttpStatus;

public class GeneralException extends RuntimeException{
    private BaseErrorCode code;

    public ErrorReasonDto getErrorReason(){
        return this.code.getReason();
    }

    public ErrorReasonDto getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
