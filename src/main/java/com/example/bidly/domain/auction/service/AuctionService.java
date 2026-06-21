package com.example.bidly.domain.auction.service;

import com.example.bidly.domain.auction.dto.request.CreateAuctionRequest;
import com.example.bidly.domain.auction.dto.response.AuctionResponse;
import com.example.bidly.domain.auction.entity.Auction;
import com.example.bidly.domain.auction.enums.AuctionStatus;
import com.example.bidly.domain.auction.repository.AuctionRepository;
import com.example.bidly.domain.bid.repository.BidRepository;
import com.example.bidly.domain.notification.enums.NotificationType;
import com.example.bidly.domain.notification.event.NotificationEvent;
import com.example.bidly.domain.product.entity.Product;
import com.example.bidly.domain.product.repository.ProductRepository;
import com.example.bidly.global.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.bidly.domain.product.enums.ProductStatus.ON_SALE;
import static com.example.bidly.domain.product.enums.ProductStatus.SOLD_OUT;
import static com.example.bidly.global.exception.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final ProductRepository productRepository;
    private final BidRepository bidRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createAuction(CreateAuctionRequest request) {
        Product findProduct = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ServerException(PRODUCT_NOT_FOUND));

        Auction savedAuction = Auction.builder()
                .startPrice(request.getStartPrice())
                .product(findProduct)
                .bidCount(0)
                .currentPrice(0L)
                .duration(request.getDuration())
                .status(AuctionStatus.ACTIVE)
                .build();
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

    @Transactional
    public void closeExpiredAuctions() {
        List<Auction> expiredAuctions = auctionRepository
                .findByEndAtBeforeAndStatus(LocalDateTime.now(), AuctionStatus.ACTIVE);

        for (Auction auction : expiredAuctions) {
            closeAuction(auction);
        }
    }

    @Transactional
    public void closeAuction(Auction auction) {
        auction.close();

        bidRepository.findTopByAuctionOrderByBidPriceDesc(auction)
                .ifPresentOrElse(winnerBid -> {
                    auction.getProduct().updateStatus(SOLD_OUT);

                    eventPublisher.publishEvent(new NotificationEvent(
                            winnerBid.getBidder().getId(),
                            NotificationType.AUCTION_WON,
                            auction.getProduct().getTitle() + " 경매에 낙찰됐습니다."
                    ));

                    eventPublisher.publishEvent(new NotificationEvent(
                            auction.getProduct().getSeller().getId(),
                            NotificationType.AUCTION_WON,
                            auction.getProduct().getTitle() + " 경매가 낙찰됐습니다."
                    ));
                    log.info("경매 낙찰 처리 완료 - auctionId: {}, winnerId: {}", auction.getId(), winnerBid.getBidder().getId());
                },
                () -> {
                    auction.getProduct().updateStatus(ON_SALE);

                    eventPublisher.publishEvent(new NotificationEvent(
                           auction.getProduct().getSeller().getId(),
                           NotificationType.AUCTION_CLOSED,
                           auction.getProduct().getTitle() + " 경매가 유찰됐습니다."
                    ));
                    log.info("경매 유찰 처리 완료 - auctionId: {}", auction.getId());
                });
    }
}
