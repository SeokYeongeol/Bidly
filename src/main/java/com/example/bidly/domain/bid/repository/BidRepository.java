package com.example.bidly.domain.bid.repository;

import com.example.bidly.domain.auction.entity.Auction;
import com.example.bidly.domain.bid.entity.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BidRepository extends JpaRepository<Bid, Long> {
    Optional<Bid> findTopByAuctionOrderByBidPriceDesc(Auction auction);

    boolean existsByAuction(Auction auction);
}
