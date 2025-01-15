package com.easystock.backend.aspect.exception;

import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;

public class UnauthorizedTokenException extends GeneralException{

    public UnauthorizedTokenException(ErrorStatus errorStatus) {
        super(errorStatus);
    }

    public UnauthorizedTokenException(){
        super(ErrorStatus.TOKEN_INVALID_TYPE);
    }
}
