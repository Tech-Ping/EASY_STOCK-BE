package com.easystock.backend.aspect.exception;

import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;

public class QuizException extends GeneralException {
    public QuizException(ErrorStatus errorStatus) { super(errorStatus); }

}
