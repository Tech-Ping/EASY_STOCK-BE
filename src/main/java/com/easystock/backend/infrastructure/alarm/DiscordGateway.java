package com.easystock.backend.infrastructure.alarm;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DiscordGateway {
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate(); // RestTemplate 재사용

    @Value("${discord.notification-url}")
    private String discordNotificationUrl;
    @SneakyThrows
    public void sendNotificationDiscord(DiscordMessageDto dto) {
        String body = objectMapper.writeValueAsString(dto);
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<String> requestEntity = RequestEntity
                .post(discordNotificationUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body);

        restTemplate.exchange(requestEntity, Void.class);
    }

    @Builder
    public static class DiscordMessageDto {
        @JsonProperty("embeds")
        final List<DiscordEmbedDto> embeds;
    }

    @Builder
    public static class DiscordEmbedDto {
        @JsonProperty("title")
        final String title;
        @JsonProperty("description")
        final String description;
        @JsonProperty("color")
        int color = 16711680;
        @JsonProperty("fields")
        final List<DiscordFieldDto> fields;
    }

    @Builder
    public static class DiscordFieldDto {
        @JsonProperty("name")
        final String name;
        @JsonProperty("value")
        final String value;
        @JsonProperty("inline")
        final boolean inline;
    }
}
