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

    private Integer startPrice;
    private Integer currentPrice;
    private Integer bidCount;
    private LocalDateTime endAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Builder
    public Auction(
        Integer startPrice,
        Integer currentPrice,
        Integer bidCount,
        Product product,
        AuctionDuration duration
    )
    {
        this.startPrice = startPrice;
        this.currentPrice = currentPrice;
        this.bidCount = bidCount;
        this.product = product;
        this.endAt = LocalDateTime.now().plusDays(duration.getDays());
    }
}
