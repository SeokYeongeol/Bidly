package com.example.bidly.domain.product.event;

import com.example.bidly.domain.auction.enums.AuctionDuration;
import com.example.bidly.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductCreatedEvent {

    private final Product product;
    private final Long startPrice;
    private final AuctionDuration duration;
}
