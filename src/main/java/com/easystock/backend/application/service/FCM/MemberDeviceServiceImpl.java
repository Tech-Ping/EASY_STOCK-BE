package com.easystock.backend.application.service.FCM;

import com.easystock.backend.infrastructure.database.entity.MemberDevice;
import com.easystock.backend.infrastructure.database.entity.key.MemberDeviceKey;
import com.easystock.backend.infrastructure.database.repository.MemberDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberDeviceServiceImpl implements MemberDeviceService {
    private final MemberDeviceRepository memberDeviceRepository;

    @Transactional
    public boolean addDevice(Long memberId, String fcmToken) {
        if (memberDeviceRepository.existsById(new MemberDeviceKey(memberId, fcmToken)))
            return false;
        memberDeviceRepository.deleteByFcmToken(fcmToken);
        memberDeviceRepository.save(
                new MemberDevice(memberId, fcmToken)
        );
        return true;
    }

    @Transactional
    public boolean removeDevice(Long memberId, String fcmToken) {
        if (!memberDeviceRepository.existsById(new MemberDeviceKey(memberId, fcmToken))) {
            return false;
        }
        memberDeviceRepository.deleteById(new MemberDeviceKey(memberId, fcmToken));
        return true;
    }

    public List<String> getAllTokensByMemberId(Long memberId) {
        return memberDeviceRepository.findAllByMemberId(memberId)
                .stream()
                .map(MemberDevice::getFcmToken)
                .toList();
    }

    @Transactional
    public void removeAllDevicesByMemberId(Long memberId) {
        memberDeviceRepository.deleteAllByMemberId(memberId);
    }
}
