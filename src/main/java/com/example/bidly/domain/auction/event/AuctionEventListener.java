package com.example.bidly.domain.auction.event;

import com.example.bidly.domain.auction.dto.request.CreateAuctionRequest;
import com.example.bidly.domain.auction.service.AuctionService;
import com.example.bidly.domain.product.event.ProductCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class AuctionEventListener {

    private final AuctionService auctionService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProductCreated(ProductCreatedEvent event) {
        auctionService.createAuction(
                CreateAuctionRequest.from(event)
        );
    }
}
