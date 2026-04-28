package com.example.bidly.domain.product.dto.response;

import com.example.bidly.domain.auction.entity.Auction;
import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.product.entity.Product;
import com.example.bidly.domain.product.enums.ProductCategory;
import com.example.bidly.domain.product.enums.ProductStatus;
import com.example.bidly.domain.product.enums.TradeType;
import com.example.bidly.domain.productimage.entity.ProductImage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ProductResponse {

    private final Long id;
    private final String title;
    private final String description;
    private final ProductCategory category;
    private final ProductStatus status;
    private final TradeType type;
    private final AuctionInfo auction;
    private final List<String> imageUrls;
    private final SellerInfo seller;
    private final LocalDateTime createdAt;

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .category(product.getCategory())
                .status(product.getStatus())
                .type(product.getType())
                .imageUrls(product.getImages().stream()
                        .map(ProductImage::getImageUrl)
                        .toList())
                .seller(SellerInfo.of(product.getSeller()))
                .auction(product.getAuction() != null
                        ? AuctionInfo.of(product.getAuction())
                        : null)
                .createdAt(product.getCreatedAt())
                .build();
    }

    @Getter
    @Builder
    public static class AuctionInfo {
        private Long auctionId;
        private Integer startPrice;
        private Integer currentPrice;
        private Integer bidCount;
        private LocalDateTime endAt;

        public static AuctionInfo of(Auction auction) {
            return AuctionInfo.builder()
                    .auctionId(auction.getId())
                    .startPrice(auction.getStartPrice())
                    .currentPrice(auction.getCurrentPrice())
                    .bidCount(auction.getBidCount())
                    .endAt(auction.getEndAt())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class SellerInfo {
        private Long sellerId;
        private String sellerName;

        public static SellerInfo of(Member member) {
            return SellerInfo.builder()
                    .sellerId(member.getId())
                    .sellerName(member.getName())
                    .build();
        }
    }
}