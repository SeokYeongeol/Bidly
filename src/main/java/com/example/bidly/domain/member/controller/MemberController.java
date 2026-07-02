package com.example.bidly.domain.member.controller;

import com.example.bidly.domain.member.dto.request.ChangePasswordRequest;
import com.example.bidly.domain.member.dto.request.DeleteMemberRequest;
import com.example.bidly.domain.member.dto.request.NameSetRequest;
import com.example.bidly.domain.member.service.MemberService;
import com.example.bidly.global.entity.Auth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/v1/members")
    public ResponseEntity<String> changePassword(
            @AuthenticationPrincipal Auth auth,
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        memberService.changePassword(auth, request);
        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    @PatchMapping("/v1/members/name")
    public ResponseEntity<Void> setName(
            @AuthenticationPrincipal Auth auth,
            @Valid @RequestBody NameSetRequest request
    ) {
        memberService.setName(auth, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/v1/members")
    public ResponseEntity<String> deleteMember(
            @AuthenticationPrincipal Auth auth,
            @Valid @RequestBody DeleteMemberRequest request
    ) {
        memberService.deleteMember(auth, request);
        return ResponseEntity.ok("삭제되었습니다.");
    }
}
