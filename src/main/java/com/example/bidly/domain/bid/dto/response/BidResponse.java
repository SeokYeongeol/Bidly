package com.example.bidly.domain.bid.dto.response;

import com.example.bidly.domain.bid.entity.Bid;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BidResponse {

    private Long id;
    private Long bidderId;
    private Long bidPrice;
    private Long auctionId;

    public static BidResponse of(Bid bid) {
        return BidResponse.builder()
            .id(bid.getId())
            .bidderId(bid.getBidder().getId())
            .bidPrice(bid.getBidPrice())
            .auctionId(bid.getAuction().getId())
            .build();
    }
}
