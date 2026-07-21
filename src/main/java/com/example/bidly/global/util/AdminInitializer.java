package com.example.bidly.global.util;

import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.member.repository.MemberRepository;
import com.example.bidly.domain.member.role.MemberRole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.password}")
    private String adminPassword;

    @PostConstruct
    public void init() {
        if (memberRepository.findByEmail(adminEmail).isPresent()) return;

        Member admin = Member.builder()
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .name("admin")
                .role(MemberRole.ROLE_ADMIN)
                .build();
        memberRepository.save(admin);
    }
}
