package com.example.bidly.domain.auction.dto.request;

import com.example.bidly.domain.auction.enums.AuctionDuration;
import com.example.bidly.domain.product.event.ProductCreatedEvent;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateAuctionRequest {

    private Long productId;
    private Integer startPrice;
    private AuctionDuration duration;

    public static CreateAuctionRequest from(ProductCreatedEvent event) {
        return CreateAuctionRequest.builder()
                .productId(event.getProduct().getId())
                .startPrice(event.getStartPrice())
                .duration(event.getDuration())
                .build();
    }
}
