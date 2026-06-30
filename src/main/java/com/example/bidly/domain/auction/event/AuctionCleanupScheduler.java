package com.example.bidly.domain.auction.event;

import com.example.bidly.domain.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuctionCleanupScheduler {

    private final AuctionService auctionService;

    @Scheduled(cron = "0 0 4 * * *")
    public void hardDeleteCancelledAuctions() {
        log.info("취소된 경매 하드 딜리트 스케줄러 실행");
        auctionService.hardDeleteCancelledAuctions();
    }
}
