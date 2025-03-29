package com.easystock.backend.application.service.fcm;


public interface MemberDeviceService {
    boolean addDevice(Long memberId, String fcmToken);
    boolean removeDevice(Long memberId, String fcmToken);
    void removeAllDevicesByMemberId(Long memberId);
}
