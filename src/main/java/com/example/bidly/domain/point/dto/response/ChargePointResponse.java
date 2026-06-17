package com.example.bidly.domain.point.dto.response;

import com.example.bidly.domain.point.entity.PointPayment;
import com.example.bidly.domain.point.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChargePointResponse {

    private final Long currentBalance;
    private final Long chargedAmount;
    private final PaymentStatus status;
}
