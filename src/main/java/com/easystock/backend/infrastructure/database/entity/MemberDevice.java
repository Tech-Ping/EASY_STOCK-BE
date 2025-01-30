package com.easystock.backend.infrastructure.database.entity;

import com.easystock.backend.infrastructure.database.entity.common.AuditingEntity;
import com.easystock.backend.infrastructure.database.entity.key.MemberDeviceKey;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity(name = "member_device")
@IdClass(MemberDeviceKey.class)
public class MemberDevice extends AuditingEntity {
    @Id
    @Column(name = "member_id")
    private Long memberId;

    @Id
    @Column(name = "fcm_token")
    private String fcmToken;
}
