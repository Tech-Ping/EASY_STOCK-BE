package com.easystock.backend.infrastructure.alarm;

import com.easystock.backend.presentation.api.dto.request.BulkNotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class ApplicationListener {
    private final DiscordGateway discordGateway;

    @Async
    @EventListener
    public void onNotificationSent(BulkNotificationEvent event) {

        DiscordGateway.DiscordMessageDto dto = DiscordGateway.DiscordMessageDto.builder()
                .embeds(List.of(
                        DiscordGateway.DiscordEmbedDto.builder()
                                .title("🔔 알림 발송 리포트")
                                .description("백엔드 서버에서 FCM 벌크 알림이 발송되었습니다.")
                                .color(65280) // 🔹 초록색
                                .fields(List.of(
                                        DiscordGateway.DiscordFieldDto.builder()
                                                .name("📌 알림 종류")
                                                .value(event.alarmType())
                                                .inline(false)
                                                .build(),
                                        DiscordGateway.DiscordFieldDto.builder()
                                                .name("📱 알림 발송 대상 기기 수")
                                                .value(String.valueOf(event.totalTargets()))
                                                .inline(true)
                                                .build(),
                                        DiscordGateway.DiscordFieldDto.builder()
                                                .name("👤 알림 발송 대상 사용자 수")
                                                .value(String.valueOf(event.totalMembers()))
                                                .inline(true)
                                                .build(),
                                        DiscordGateway.DiscordFieldDto.builder()
                                                .name("⏳ 소요시간 (밀리초)")
                                                .value(String.valueOf(event.elapsedMilliSec()))
                                                .inline(true)
                                                .build()
                                ))
                                .build()
                ))
                .build();

        discordGateway.sendNotificationDiscord(dto);
    }
}
