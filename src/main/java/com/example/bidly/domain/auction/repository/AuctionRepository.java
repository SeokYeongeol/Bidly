package com.example.bidly.domain.auction.repository;

import com.example.bidly.domain.auction.entity.Auction;
import com.example.bidly.domain.product.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuctionRepository extends JpaRepository<Auction, Long> {

    @Query("select a from Auction a where a.product.status = :productStatus")
    Page<Auction> findAllByProductStatus(@Param("productStatus") ProductStatus productStatus, Pageable pageable);
}
