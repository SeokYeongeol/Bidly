package com.example.bidly.domain.member.entity;

import com.example.bidly.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailVerification extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private boolean verified;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Builder
    public EmailVerification(String email, String code) {
        this.email = email;
        this.code = code;
        this.verified = false;
        this.expiredAt = LocalDateTime.now().plusMinutes(5);
    }

    public void verify() { this.verified = true; }

    public boolean isExpired() { return LocalDateTime.now().isAfter(this.expiredAt); }
}
