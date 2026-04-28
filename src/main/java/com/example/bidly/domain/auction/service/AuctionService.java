package com.example.bidly.domain.auction.service;

import com.example.bidly.domain.auction.dto.request.CreateAuctionRequest;
import com.example.bidly.domain.auction.dto.response.AuctionResponse;
import com.example.bidly.domain.auction.entity.Auction;
import com.example.bidly.domain.auction.repository.AuctionRepository;
import com.example.bidly.domain.product.entity.Product;
import com.example.bidly.domain.product.repository.ProductRepository;
import com.example.bidly.global.exception.ServerException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.bidly.global.exception.ErrorCode.PRODUCT_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void createAuction(CreateAuctionRequest request) {
        Product findProduct = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ServerException(PRODUCT_NOT_FOUND));

        Auction savedAuction = new Auction(request.getStartPrice(), findProduct, request.getDuration());
        auctionRepository.save(savedAuction);
    }
}
