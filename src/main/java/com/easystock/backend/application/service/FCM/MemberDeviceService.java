package com.easystock.backend.application.service.FCM;

import com.easystock.backend.infrastructure.database.repository.MemberDeviceRepository;


public interface MemberDeviceService {
    boolean addDevice(Long memberId, String fcmToken);
    boolean removeDevice(Long memberId, String fcmToken);
    void removeAllDevicesByMemberId(Long memberId);
}
