package com.example.bidly.domain.auth.service;

import com.example.bidly.domain.member.entity.EmailVerification;
import com.example.bidly.domain.member.repository.EmailVerificationRepository;
import com.example.bidly.global.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.bidly.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final JavaMailSender mailSender;
    private final EmailVerificationRepository emailVerificationRepository;

    @Transactional
    public void sendVerificationCode(String email) {
        // 기존 인증 코드 삭제
        emailVerificationRepository.deleteByEmail(email);

        // 6자리 인증 코드 생성
        String code = String.format("%06d", (int)(Math.random() * 1000000));

        EmailVerification verification = EmailVerification.builder()
                .email(email)
                .code(code)
                .build();
        emailVerificationRepository.save(verification);

        // 이메일 발송
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("[Bidly] 이메일 인증 코드");
        message.setText(
                "안녕하세요! Bidly입니다.\n\n" +
                        "인증 코드: " + code + "\n\n" +
                        "5분 안에 입력해주세요.\n" +
                        "본인이 요청하지 않았다면 무시해주세요."
        );
        mailSender.send(message);
    }

    @Transactional
    public void verifyCode(String email, String code) {
        EmailVerification verification = emailVerificationRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new ServerException(EMAIL_VERIFICATION_NOT_FOUND));

        if (verification.isExpired()) {
            throw new ServerException(EMAIL_VERIFICATION_EXPIRED);
        }

        if (!verification.getCode().equals(code)) {
            throw new ServerException(EMAIL_VERIFICATION_INVALID_CODE);
        }

        verification.verify();
    }

    public void checkVerified(String email) {
        EmailVerification verification = emailVerificationRepository
                .findTopByEmailOrderByCreatedAtDesc(email)
                .orElseThrow(() -> new ServerException(EMAIL_NOT_VERIFIED));

        if (!verification.isVerified()) {
            throw new ServerException(EMAIL_NOT_VERIFIED);
        }
    }
}