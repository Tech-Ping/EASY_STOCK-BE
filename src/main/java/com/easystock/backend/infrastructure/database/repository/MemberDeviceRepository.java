package com.easystock.backend.infrastructure.database.repository;

import com.easystock.backend.infrastructure.database.entity.MemberDevice;
import com.easystock.backend.infrastructure.database.entity.key.MemberDeviceKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberDeviceRepository extends JpaRepository<MemberDevice, MemberDeviceKey> {
    List<MemberDevice> findAllByMemberId(Long memberId);
    void deleteByFcmToken(String fcmToken);
    void deleteAllByMemberId(Long memberId);
}
