package com.example.bidly.domain.auction.controller;

import com.example.bidly.domain.auction.dto.response.AuctionResponse;
import com.example.bidly.domain.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuctionController {

    private final AuctionService auctionService;

    @GetMapping("/v1/auctions/{auctionId}")
    public ResponseEntity<AuctionResponse> findAuction(@PathVariable Long auctionId) {
        return ResponseEntity.ok(auctionService.findAuction(auctionId));
    }

    @GetMapping("/v1/auctions")
    public ResponseEntity<PagedModel<AuctionResponse>> findAllAuctions(@RequestParam int page) {
        return ResponseEntity.ok(auctionService.findAllAuctions(page));
    }
}
