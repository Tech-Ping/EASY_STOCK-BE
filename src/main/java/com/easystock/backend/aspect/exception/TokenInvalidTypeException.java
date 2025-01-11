package com.easystock.backend.aspect.exception;

import com.easystock.backend.aspect.payload.code.status.ErrorStatus;

public class TokenInvalidTypeException extends GeneralException{
    public TokenInvalidTypeException(ErrorStatus errorStatus){
        super(errorStatus);
    }
    public TokenInvalidTypeException(){
        super(ErrorStatus.TOKEN_INVALID_TYPE);
    }
}
