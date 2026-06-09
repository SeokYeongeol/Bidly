package com.example.bidly.domain.bid.service;

import com.example.bidly.domain.auction.entity.Auction;
import com.example.bidly.domain.auction.repository.AuctionRepository;
import com.example.bidly.domain.bid.dto.request.CreateBidRequest;
import com.example.bidly.domain.bid.dto.response.BidResponse;
import com.example.bidly.domain.bid.entity.Bid;
import com.example.bidly.domain.bid.repository.BidRepository;
import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.global.entity.Auth;
import com.example.bidly.global.exception.ServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.bidly.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class BidService {

    private final BidRepository bidRepository;
    private final AuctionRepository auctionRepository;

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
}
