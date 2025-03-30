package com.easystock.backend.aspect.exception;

import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;

public class ReportException extends GeneralException {
    public ReportException(ErrorStatus errorStatus) { super(errorStatus); }
}
