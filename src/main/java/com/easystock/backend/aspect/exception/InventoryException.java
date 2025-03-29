package com.easystock.backend.aspect.exception;

import com.easystock.backend.presentation.api.payload.code.status.ErrorStatus;

public class InventoryException extends GeneralException {
    public InventoryException(ErrorStatus errorStatus) { super(errorStatus); }
}
