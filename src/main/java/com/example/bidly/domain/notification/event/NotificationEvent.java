package com.example.bidly.domain.notification.event;

import com.example.bidly.domain.notification.enums.NotificationType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class NotificationEvent {

    private final Long receiverId;
    private final NotificationType type;
    private final String message;
}
