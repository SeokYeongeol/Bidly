package com.example.bidly.domain.notification.service;

import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.member.repository.MemberRepository;
import com.example.bidly.domain.notification.dto.response.NotificationResponse;
import com.example.bidly.domain.notification.entity.Notification;
import com.example.bidly.domain.notification.event.NotificationEvent;
import com.example.bidly.domain.notification.repository.NotificationRepository;
import com.example.bidly.global.entity.Auth;
import com.example.bidly.global.exception.ServerException;
import com.example.bidly.global.util.SseEmitterManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

import static com.example.bidly.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberRepository memberRepository;
    private final SseEmitterManager sseEmitterManager;
    private final ApplicationEventPublisher eventPublisher;

    // SSE 구독
    public SseEmitter subscribe(Auth auth) {
        SseEmitter emitter = sseEmitterManager.add(auth.getId());

        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected"));
        } catch (IOException ie) {
            emitter.completeWithError(ie);
        }
        return emitter;
    }

    // 이벤트 수신 → DB 저장 + SSE 푸시
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleNotification(NotificationEvent event) {
        Member findMember = memberRepository.findMemberById(event.getReceiverId())
                .orElseThrow(() -> new ServerException(USER_NOT_FOUND));

        Notification savedNotification = Notification.builder()
                .member(findMember)
                .type(event.getType())
                .message(event.getMessage())
                .build();
        notificationRepository.save(savedNotification);

        sseEmitterManager.send(event.getReceiverId(), NotificationResponse.of(savedNotification));
    }

    // 안 읽은 알림 목록
    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadNotifications(Auth auth) {
        return notificationRepository
                .findByMemberIdAndIsReadFalseOrderByCreatedAtDesc(auth.getId())
                .stream()
                .map(NotificationResponse::of)
                .toList();
    }

    // 전체 알림 목록
    @Transactional(readOnly = true)
    public PagedModel<NotificationResponse> getAllNotifications(Auth auth, int page) {
        Pageable pageable = PageRequest.of(page - 1, 10,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<NotificationResponse> responsePage = notificationRepository
                .findByMemberIdOrderByCreatedAtDesc(auth.getId(), pageable)
                .map(NotificationResponse::of);

        return new PagedModel<>(responsePage);
    }

    // 알림 읽음 처리
    @Transactional
    public void readNotification(Auth auth, Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ServerException(NOTIFICATION_NOT_FOUND));

        if (!notification.getMember().getId().equals(auth.getId())) {
            throw new ServerException(NOTIFICATION_NOT_EQUALS_USER);
        }
        notification.read();
    }

        // 전체 읽음 처리
    @Transactional
    public void readAllNotifications(Auth auth) {
        notificationRepository
                .findByMemberIdAndIsReadFalseOrderByCreatedAtDesc(auth.getId())
                .forEach(Notification::read);
    }
}
