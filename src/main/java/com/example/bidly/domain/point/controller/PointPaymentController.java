package com.example.bidly.domain.point.controller;

import com.example.bidly.domain.point.dto.request.ChargePointRequest;
import com.example.bidly.domain.point.dto.response.ChargePointResponse;
import com.example.bidly.domain.point.service.PointPaymentService;
import com.example.bidly.global.entity.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class PointPaymentController {

    private final PointPaymentService pointPaymentService;

    @PostMapping("/v1/points/charge")
    public ResponseEntity<ChargePointResponse> portOnePayment(
            @AuthenticationPrincipal Auth auth,
            @RequestBody ChargePointRequest request
    ) {
        return ResponseEntity.ok(pointPaymentService.portOnePayment(auth, request));
    }
}
