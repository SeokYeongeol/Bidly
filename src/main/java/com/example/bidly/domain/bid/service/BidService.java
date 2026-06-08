package com.example.bidly.domain.bid.service;

import com.example.bidly.domain.bid.dto.request.CreateBidRequest;
import com.example.bidly.domain.bid.dto.response.BidResponse;
import com.example.bidly.domain.bid.repository.BidRepository;
import com.example.bidly.global.entity.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BidService {

    private final BidRepository bidRepository;

    public BidResponse createBid(Auth auth, CreateBidRequest request) {
        return null;
    }
}
