package com.example.bidly.global.util;

import com.example.bidly.domain.notification.dto.response.NotificationResponse;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterManager {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter add(Long memberId) {
        // 1시간 타임 아웃
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L);

        emitters.put(memberId, emitter);

        // 연결 끊기면 제거
        emitter.onCompletion(() -> emitters.remove(memberId));
        emitter.onTimeout(() -> emitters.remove(memberId));
        emitter.onError(e -> emitters.remove(memberId));

        return emitter;
    }

    public void send(Long memberId, NotificationResponse notification) {
        SseEmitter emitter = emitters.get(memberId);
        if (emitter == null) return;

        try {
            emitter.send(SseEmitter.event()
                    .name("notification")
                    .data(notification));
        } catch (IOException io) {
            emitters.remove(memberId);
        }
    }
}
