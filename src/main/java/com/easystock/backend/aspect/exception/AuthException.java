package com.easystock.backend.aspect.exception;

import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;

public class AuthException extends GeneralException{
    public AuthException(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}
