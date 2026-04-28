package com.example.bidly.domain.auction.controller;

import com.example.bidly.domain.auction.dto.request.CreateAuctionRequest;
import com.example.bidly.domain.auction.dto.response.AuctionResponse;
import com.example.bidly.domain.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AuctionController {

    private final AuctionService auctionService;

}
