package com.example.bidly.global.service;

import com.example.bidly.domain.point.dto.response.PortOnePaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class PortOneService {

    private final RestClient portOneRestClient;

    @Value("${portone.api.secret}")
    private String apiSecret;

    public PortOnePaymentResponse getPayment(String paymentId) {
        return portOneRestClient.get()
                .uri("/payments/{paymentId}", paymentId)
                .header("Authorization", "PortOne " + apiSecret)
                .retrieve()
                .body(PortOnePaymentResponse.class);
    }
}
