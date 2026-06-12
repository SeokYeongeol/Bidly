package com.example.bidly.domain.point.controller;

import com.example.bidly.domain.point.dto.request.ExchangePointRequest;
import com.example.bidly.domain.point.dto.response.PointResponse;
import com.example.bidly.domain.point.service.PointService;
import com.example.bidly.global.entity.Auth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PointController {

    private final PointService pointService;

    @PostMapping("/v1/points/exchange")
    public ResponseEntity<PointResponse> exchangePoint(
        @AuthenticationPrincipal Auth auth,
        @Valid @RequestBody ExchangePointRequest request
    ) {
        return ResponseEntity.ok(pointService.exchangePoint(auth, request));
    }
}
