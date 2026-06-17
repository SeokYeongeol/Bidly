package com.example.bidly.domain.bid.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateBidRequest {

    @NotNull(message = "가격을 입력해주세요.")
    private Long bidPrice;
}
