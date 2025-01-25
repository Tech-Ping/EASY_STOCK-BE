package com.easystock.backend.aspect.exception;

import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;

public class StockException extends GeneralException{
    public StockException(ErrorStatus errorStatus) { super(errorStatus); }

}
