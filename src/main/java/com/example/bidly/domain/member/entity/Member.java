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

    private LocalDateTime deletedAt;

    @Builder
    public Member(String email, String password, String name, MemberRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public void changePassword(String password) { this.password = password; }
    public void deleteMember() { this.deletedAt = LocalDateTime.now(); }
    private Member(Long id) { this.id = id; }
    public static Member fromAuth(Long authId) { return new Member(authId); }
}
