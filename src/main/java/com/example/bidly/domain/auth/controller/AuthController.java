package com.example.bidly.domain.auth.controller;

import com.example.bidly.domain.auth.dto.request.EmailRequest;
import com.example.bidly.domain.auth.dto.request.EmailVerifyRequest;
import com.example.bidly.domain.auth.dto.request.LoginRequest;
import com.example.bidly.domain.auth.dto.request.SignUpRequest;
import com.example.bidly.domain.auth.dto.response.AuthResponse;
import com.example.bidly.domain.auth.service.AuthService;
import com.example.bidly.domain.auth.service.EmailVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/v1/auth/signup")
    public ResponseEntity<AuthResponse> signUp(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.ok(authService.signUp(request));
    }

    @PostMapping("/v1/auth/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // 인증 코드 발송
    @PostMapping("/v1/auth/email/send")
    public ResponseEntity<Void> sendVerificationCode(@RequestBody EmailRequest request) {
        emailVerificationService.sendVerificationCode(request.getEmail());
        return ResponseEntity.ok().build();
    }

    // 인증 코드 확인
    @PostMapping("/v1/auth/email/verify")
    public ResponseEntity<Void> verifyCode(@RequestBody EmailVerifyRequest request) {
        emailVerificationService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok().build();
    }
}
