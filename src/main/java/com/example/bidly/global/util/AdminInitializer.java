package com.example.bidly.global.util;

import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.member.repository.MemberRepository;
import com.example.bidly.domain.member.role.MemberRole;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (memberRepository.findByEmail("admin@bidly.com").isPresent()) return;

        Member admin = Member.builder()
                .email("admin@bidly.com")
                .password(passwordEncoder.encode("admin1234@"))
                .name("admin")
                .role(MemberRole.ROLE_ADMIN)
                .build();
        memberRepository.save(admin);
    }
}
