package com.example.bidly.domain.point.dto.response;

import lombok.Getter;

@Getter
public class PortOnePaymentResponse {

    private String status;
    private Amount amount;

    @Getter
    public static class Amount {
        private Long total;
    }
}
