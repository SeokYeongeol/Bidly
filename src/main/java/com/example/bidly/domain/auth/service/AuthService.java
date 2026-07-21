package com.example.bidly.domain.auth.service;

import com.example.bidly.domain.auth.dto.request.LoginRequest;
import com.example.bidly.domain.auth.dto.request.SignUpRequest;
import com.example.bidly.domain.auth.dto.response.AuthResponse;
import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.member.event.MemberCreatedEvent;
import com.example.bidly.domain.member.repository.EmailVerificationRepository;
import com.example.bidly.domain.member.repository.MemberRepository;
import com.example.bidly.domain.member.role.MemberRole;
import com.example.bidly.global.exception.ServerException;
import com.example.bidly.global.util.JwtUtil;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.bidly.domain.member.role.MemberRole.ROLE_MEMBER;
import static com.example.bidly.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailVerificationService emailVerificationService;
    private final EmailVerificationRepository emailVerificationRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void signUp(SignUpRequest request) {
        // 이메일 인증 여부 확인
        emailVerificationService.checkVerified(request.getEmail());

        if (!request.validRePassword()) {
            throw new ServerException(INVALID_PASSWORD);
        }

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new ServerException(USER_EMAIL_DUPLICATION);
        }

        if (memberRepository.existsByName(request.getName())) {
            throw new ServerException(USER_NAME_DUPLICATION);
        }

        Member savedMember = Member.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(MemberRole.of(String.valueOf(ROLE_MEMBER)))
                .build();
        memberRepository.save(savedMember);

        eventPublisher.publishEvent(new MemberCreatedEvent(savedMember.getId()));

        emailVerificationRepository.deleteByEmail(request.getEmail());
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        Member findMember = memberRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new ServerException(USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), findMember.getPassword())) {
            throw new ServerException(INVALID_PASSWORD);
        }

        String accessToken = jwtUtil.createAccessToken(
                findMember.getId(),
                findMember.getEmail(),
                findMember.getRole()
        );
        return new AuthResponse(accessToken);
    }
}