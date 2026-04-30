package com.example.bidly.domain.auction.dto.response;

import com.example.bidly.domain.auction.entity.Auction;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class AuctionResponse {
    private final Long id;
    private final String productName;
    private final String productDescription;
    private final Integer startPrice;
    private final Integer currentPrice;
    private final Integer bidCount;
    private final LocalDateTime endAt;

    public static AuctionResponse of(Auction auction) {
        return AuctionResponse.builder()
                .id(auction.getId())
                .productName(auction.getProduct().getTitle())
                .productDescription(auction.getProduct().getDescription())
                .startPrice(auction.getStartPrice())
                .currentPrice(auction.getCurrentPrice())
                .bidCount(auction.getBidCount())
                .endAt(auction.getEndAt())
                .build();
    }
}
