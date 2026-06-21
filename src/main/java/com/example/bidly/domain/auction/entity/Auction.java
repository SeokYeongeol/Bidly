package com.example.bidly.domain.auction.entity;

import com.example.bidly.domain.auction.enums.AuctionDuration;
import com.example.bidly.domain.auction.enums.AuctionStatus;
import com.example.bidly.domain.product.entity.Product;
import com.example.bidly.global.entity.TimeStamped;
import com.example.bidly.global.exception.ServerException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.example.bidly.global.exception.ErrorCode.AUCTION_ALREADY_CLOSED;

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

    @Enumerated(EnumType.STRING)
    private AuctionStatus status;

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
        AuctionDuration duration,
        AuctionStatus status
    )
    {
        this.startPrice = startPrice;
        this.product = product;
        this.bidCount = bidCount;
        this.currentPrice = currentPrice;
        this.status = status;
        this.endAt = LocalDateTime.now().plusDays(duration.getDays());
    }

    public void changeCurrentPrice(Long currentPrice) { this.currentPrice = currentPrice; }

    public void plusBidCount() { this.bidCount++; }

    public void close() {
        if (!this.status.equals(AuctionStatus.ACTIVE)) {
            throw new ServerException(AUCTION_ALREADY_CLOSED);
        }
        this.status = AuctionStatus.CLOSE;
    }

    public void cancel() {
        if (!this.status.equals(AuctionStatus.ACTIVE)) {
            throw new ServerException(AUCTION_ALREADY_CLOSED);
        }
        this.status = AuctionStatus.CANCEL;
    }

    public boolean isActive() {
        return this.status.equals(AuctionStatus.ACTIVE);
    }
}
