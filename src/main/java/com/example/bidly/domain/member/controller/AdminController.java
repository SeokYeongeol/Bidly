package com.example.bidly.domain.member.controller;

import com.example.bidly.domain.member.service.AdminService;
import com.example.bidly.global.entity.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AdminController {

    private final AdminService adminService;

    @PatchMapping("/v1/admins/{memberId}/changes")
    public ResponseEntity<Void> changeRole(
            @AuthenticationPrincipal Auth auth,
            @PathVariable Long memberId
    ) {
        adminService.changeRole(auth, memberId);
        return ResponseEntity.ok().build();
    }
}
