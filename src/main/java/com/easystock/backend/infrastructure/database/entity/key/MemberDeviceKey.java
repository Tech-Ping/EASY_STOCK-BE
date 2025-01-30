package com.easystock.backend.infrastructure.database.entity.key;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class MemberDeviceKey implements Serializable {
    private Long memberId;
    private String fcmToken;
}
