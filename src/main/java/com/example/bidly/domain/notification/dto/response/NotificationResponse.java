package com.example.bidly.domain.notification.dto.response;

import com.example.bidly.domain.notification.entity.Notification;
import com.example.bidly.domain.notification.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationResponse {

    private final Long id;
    private final NotificationType type;
    private final String message;
    private final boolean isRead;
    private final LocalDateTime createdAt;

    public static NotificationResponse of(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .type(notification.getType())
                .message(notification.getMessage())
                .isRead(notification.isRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
