package com.example.bidly.domain.product.dto.request;

import com.example.bidly.domain.auction.enums.AuctionDuration;
import com.example.bidly.domain.product.enums.ProductCategory;
import com.example.bidly.domain.product.enums.TradeType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateProductRequest {

    @NotNull(message = "제품 명을 입력해주세요.")
    private String title;

    @NotNull(message = "제품 설명을 입력해주세요.")
    private String description;

    @NotNull(message = "제품 카테고리를 설정해주세요.")
    private ProductCategory category;

    @NotNull(message = "제품 거래 타입을 선택해주세요.")
    private TradeType tradeType;

    // 즉시 구매일 경우
    private Integer price;

    // 경매일 경우
    private Integer startPrice;
    private AuctionDuration duration;
}
