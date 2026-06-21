package com.example.bidly.domain.notification.entity;

import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.notification.enums.NotificationType;
import com.example.bidly.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Notification extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private boolean isRead;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Notification(String message, NotificationType type, Member member) {
        this.message = message;
        this.type = type;
        this.member = member;
        this.isRead = false;
    }

    public void read() {
        this.isRead = true;
    }
}
