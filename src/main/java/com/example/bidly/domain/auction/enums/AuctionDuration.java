package com.example.bidly.domain.auction.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AuctionDuration {
    ONE_DAY(1), THREE_DAYS(3), SEVEN_DAYS(7);

    private final int days;
}
