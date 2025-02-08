package com.easystock.backend.infrastructure.database.entity.key;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public class MemberDeviceKey implements Serializable {
    private Long memberId;
    private String fcmToken;
}
