package com.example.bidly.domain.product.dto.response;

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
    private final Long sellerId;
    private final String title;
    private final String description;
    private final ProductCategory category;
    private final ProductStatus status;
    private final TradeType type;
    private final List<String> imageUrls;
    private final LocalDateTime createdAt;

    public static ProductResponse of(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .sellerId(product.getSeller().getId())
                .title(product.getTitle())
                .description(product.getDescription())
                .category(product.getCategory())
                .status(product.getStatus())
                .type(product.getType())
                .imageUrls(product.getImages().stream()
                        .map(ProductImage::getImageUrl)
                        .toList())
                .createdAt(product.getCreatedAt())
                .build();
    }
}