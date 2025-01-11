package com.easystock.backend.aspect.exception;

import com.easystock.backend.aspect.payload.code.status.ErrorStatus;

public class JoinException extends GeneralException {

    public JoinException(ErrorStatus errorStatus) {
        super(errorStatus);
    }
}
