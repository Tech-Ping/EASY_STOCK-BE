package com.easystock.backend.aspect.exception;

import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;

public class TutorialException extends GeneralException{
    public TutorialException(ErrorStatus errorStatus) { super(errorStatus); }
}
