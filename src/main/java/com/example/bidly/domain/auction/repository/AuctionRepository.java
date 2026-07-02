package com.example.bidly.domain.auction.repository;

import com.example.bidly.domain.auction.entity.Auction;
import com.example.bidly.domain.auction.enums.AuctionStatus;
import com.example.bidly.domain.product.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Query("select a from Auction a where a.product.status = :productStatus")
    Page<Auction> findAllByProductStatus(@Param("productStatus") ProductStatus productStatus, Pageable pageable);

    List<Auction> findByEndAtBeforeAndStatus(LocalDateTime endAtBefore, AuctionStatus status);

    List<Auction> findByStatusAndUpdatedAtBefore(AuctionStatus status, java.time.LocalDateTime updatedAtBefore);
}
