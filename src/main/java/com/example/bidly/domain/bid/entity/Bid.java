package com.example.bidly.domain.bid.entity;

import com.example.bidly.domain.auction.entity.Auction;
import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Bid extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bidPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auctions_id", nullable = false)
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bidder_id", nullable = false)
    private Member bidder;

    @Builder
    public Bid(Long bidPrice, Auction auction, Member bidder) {
        this.bidPrice = bidPrice;
        this.auction = auction;
        this.bidder = bidder;
    }
}
