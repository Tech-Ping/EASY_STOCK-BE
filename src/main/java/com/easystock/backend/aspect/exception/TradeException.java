package com.easystock.backend.aspect.exception;

import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;

public class TradeException extends GeneralException{
    public TradeException(ErrorStatus errorStatus) { super(errorStatus); }

}