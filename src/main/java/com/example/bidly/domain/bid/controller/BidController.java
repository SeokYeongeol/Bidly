package com.example.bidly.domain.bid.controller;

import com.example.bidly.domain.bid.dto.request.CreateBidRequest;
import com.example.bidly.domain.bid.dto.response.BidResponse;
import com.example.bidly.domain.bid.service.BidService;
import com.example.bidly.global.entity.Auth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BidController {

    private final BidService bidService;

    @PostMapping("/v1/auctions/{auctionId}/bids")
    public ResponseEntity<BidResponse> createBid(
            @AuthenticationPrincipal Auth auth,
            @PathVariable Long auctionId,
            @Valid @RequestBody CreateBidRequest request
    ) {
        return ResponseEntity.ok(bidService.createBid(auth, auctionId, request));
    }
}
