package com.example.bidly.domain.auction.service;

import com.example.bidly.domain.auction.dto.request.CreateAuctionRequest;
import com.example.bidly.domain.auction.dto.response.AuctionResponse;
import com.example.bidly.domain.auction.entity.Auction;
import com.example.bidly.domain.auction.repository.AuctionRepository;
import com.example.bidly.domain.product.entity.Product;
import com.example.bidly.domain.product.repository.ProductRepository;
import com.example.bidly.global.entity.Auth;
import com.example.bidly.global.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.bidly.domain.product.enums.ProductStatus.ON_SALE;
import static com.example.bidly.domain.product.enums.ProductStatus.SOLD_OUT;
import static com.example.bidly.global.exception.ErrorCode.*;

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

    @Transactional(readOnly = true)
    public AuctionResponse findAuction(Long auctionId) {
        Auction findAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ServerException(AUCTION_NOT_FOUND));

        if (findAuction.getProduct().getStatus().equals(SOLD_OUT)) {
            throw new ServerException(PRODUCT_SOLD_OUT);
        }
        return AuctionResponse.of(findAuction);
    }

    @Transactional(readOnly = true)
    public PagedModel<AuctionResponse> findAllAuctions(int page) {
        Pageable pageable = PageRequest.of(page - 1, 10,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Auction> findAuctions = auctionRepository.findAllByProductStatus(ON_SALE, pageable);
        return new PagedModel<>(findAuctions.map(AuctionResponse::of));
    }
}
