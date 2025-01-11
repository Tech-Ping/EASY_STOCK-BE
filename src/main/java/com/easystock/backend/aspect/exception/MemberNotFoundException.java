package com.easystock.backend.aspect.exception;

import com.easystock.backend.aspect.payload.code.status.ErrorStatus;

public class MemberNotFoundException extends GeneralException {

    public MemberNotFoundException(ErrorStatus errorStatus) {
        super(errorStatus);
    }

    public MemberNotFoundException(){
        super(ErrorStatus.MEMBER_NOT_FOUND);
    }
}
