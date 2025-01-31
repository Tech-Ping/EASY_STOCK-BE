package com.easystock.backend.aspect.exception;

import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;

public class LevelException extends GeneralException{
    public LevelException(ErrorStatus errorStatus) { super(errorStatus); }
}
