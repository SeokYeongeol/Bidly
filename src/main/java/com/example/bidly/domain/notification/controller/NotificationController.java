package com.example.bidly.domain.notification.controller;

import com.example.bidly.domain.notification.dto.response.NotificationResponse;
import com.example.bidly.domain.notification.service.NotificationService;
import com.example.bidly.global.entity.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/v1/notifications/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@AuthenticationPrincipal Auth auth) {
        return ResponseEntity.ok(notificationService.subscribe(auth));
    }

    @GetMapping("/v1/notifications/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(@AuthenticationPrincipal Auth auth) {
        return ResponseEntity.ok(notificationService.getUnreadNotifications(auth));
    }

    @GetMapping("/v1/notifications")
    public ResponseEntity<PagedModel<NotificationResponse>> getAllNotifications(
            @AuthenticationPrincipal Auth auth,
            @RequestParam int page
    ) {
        return ResponseEntity.ok(notificationService.getAllNotifications(auth, page));
    }

    @PatchMapping("/v1/notifications/{notificationId}/read")
    public ResponseEntity<Void> readNotification(
            @AuthenticationPrincipal Auth auth,
            @PathVariable Long notificationId
    ) {
        notificationService.readNotification(auth, notificationId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/v1/notifications/read-all")
    public ResponseEntity<Void> readAllNotifications(@AuthenticationPrincipal Auth auth) {
        notificationService.readAllNotifications(auth);
        return ResponseEntity.ok().build();
    }
}
