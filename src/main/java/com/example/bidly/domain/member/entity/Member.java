package com.example.bidly.domain.member.entity;

import com.example.bidly.domain.member.role.MemberRole;
import com.example.bidly.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true)
    private String email;
    private String password;

    @Column(length = 10, unique = true)
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String provider;
    private String providerId;
    private boolean nameSet;

    private LocalDateTime deletedAt;

    @Builder
    public Member(
            String email,
            String password,
            String name,
            MemberRole role,
            String provider,
            String providerId
    ) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.nameSet = false;
    }

    public void changePassword(String password) { this.password = password; }

    public void deleteMember() { this.deletedAt = LocalDateTime.now(); }

    public Member updateName(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
        this.nameSet = true;
    }
}
