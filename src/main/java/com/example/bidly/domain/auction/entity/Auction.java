package com.example.bidly.domain.auction.entity;

import com.example.bidly.domain.auction.enums.AuctionDuration;
import com.example.bidly.domain.product.entity.Product;
import com.example.bidly.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Auction extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long startPrice;
    private Long currentPrice;
    private Integer bidCount;
    private LocalDateTime endAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Version
    private Long version;

    @Builder
    public Auction(
        Long startPrice,
        Product product,
        Integer bidCount,
        Long currentPrice,
        AuctionDuration duration
    )
    {
        this.startPrice = startPrice;
        this.product = product;
        this.bidCount = bidCount;
        this.currentPrice = currentPrice;
        this.endAt = LocalDateTime.now().plusDays(duration.getDays());
    }

    public void changeCurrentPrice(Long currentPrice) { this.currentPrice = currentPrice; }

    public void plusBidCount() { this.bidCount++; }
}
