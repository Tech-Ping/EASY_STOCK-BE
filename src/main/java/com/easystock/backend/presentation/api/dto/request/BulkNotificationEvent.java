package com.easystock.backend.presentation.api.dto.request;

public record BulkNotificationEvent(
        String alarmType,
        int totalTargets,
        int totalMembers,
        long elapsedMilliSec
) {
}
