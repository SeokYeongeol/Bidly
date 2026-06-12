package com.example.bidly.domain.bid.service;

import com.example.bidly.domain.auction.entity.Auction;
import com.example.bidly.domain.auction.repository.AuctionRepository;
import com.example.bidly.domain.bid.dto.request.CreateBidRequest;
import com.example.bidly.domain.bid.dto.response.BidResponse;
import com.example.bidly.domain.bid.entity.Bid;
import com.example.bidly.domain.bid.repository.BidRepository;
import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.point.entity.Point;
import com.example.bidly.domain.point.entity.PointHistory;
import com.example.bidly.domain.point.enums.PointType;
import com.example.bidly.domain.point.repository.PointHistoryRepository;
import com.example.bidly.domain.point.repository.PointRepository;
import com.example.bidly.global.entity.Auth;
import com.example.bidly.global.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.bidly.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;
    private final PointRepository pointRepository;
    private final PointHistoryRepository pointHistoryRepository;

    @Retryable(retryFor = OptimisticLockingFailureException.class)
    @Transactional
    public BidResponse createBid(Auth auth, Long auctionId, CreateBidRequest request) {
        Auction findAuction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new ServerException(AUCTION_NOT_FOUND));
        Member findMember = Member.fromAuth(auth.getId());

        if (request.getBidPrice() < findAuction.getStartPrice()) {
            throw new ServerException(BID_PRICE_LOWER_START_PRICE);
        }
        if (request.getBidPrice() <= findAuction.getCurrentPrice()) {
            throw new ServerException(BID_PRICE_LOWER_CURRENT_PRICE);
        }
        if (request.getBidPrice() % 10000 != 0) {
            throw new ServerException(BID_PRICE_DIVIDE_TEN_THOUSAND);
        }
        if (findAuction.getProduct().getSeller().getId().equals(findMember.getId())) {
            throw new ServerException(SELLER_BIDDER_DUPLICATED);
        }

        refundPoint(findAuction);
        usePoint(findMember, request);

        Bid savedBid = Bid.builder()
                .bidPrice(request.getBidPrice())
                .auction(findAuction)
                .bidder(findMember)
                .build();
        bidRepository.save(savedBid);

        findAuction.changeCurrentPrice(request.getBidPrice());
        findAuction.plusBidCount();
        return BidResponse.of(savedBid);
    }

    // 이전 최고 입찰자 포인트 환불
    private void refundPoint(Auction auction) {
        bidRepository.findTopByAuctionOrderByBidPriceDesc(auction)
                .ifPresent(prevBid -> {
                    Point prevPoint = pointRepository.findPointsByMemberId(prevBid.getBidder().getId())
                            .orElseThrow(() -> new ServerException(USER_NOT_FOUND));
                    prevPoint.chargePoint(prevBid.getBidPrice());
                    PointHistory savedPointHistory = PointHistory.builder()
                            .amount(prevBid.getBidPrice())
                            .description("입찰 실패 환불")
                            .type(PointType.REFUND)
                            .point(prevPoint)
                            .build();
                    pointHistoryRepository.save(savedPointHistory);
                });
    }

    // 새 입찰자 포인트 차감
    private void usePoint(Member member, CreateBidRequest request) {
        Point findPoint = pointRepository.findPointsByMemberId(member.getId())
                .orElseThrow(() -> new ServerException(USER_NOT_FOUND));
        findPoint.usePoint(request.getBidPrice());
        PointHistory savedPointHistory = PointHistory.builder()
                .amount(request.getBidPrice())
                .point(findPoint)
                .type(PointType.USE)
                .description("입찰 사용")
                .build();
        pointHistoryRepository.save(savedPointHistory);
    }
}
