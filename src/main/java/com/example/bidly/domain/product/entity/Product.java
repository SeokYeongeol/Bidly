package com.example.bidly.domain.product.entity;

import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.product.enums.ProductCategory;
import com.example.bidly.domain.product.enums.ProductStatus;
import com.example.bidly.domain.product.enums.TradeType;
import com.example.bidly.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Product extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30)
    private String title;
    private String description;
    private Integer price;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Enumerated(EnumType.STRING)
    private TradeType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Member member;

    @Builder
    public Product(
        String title,
        String description,
        Integer price,
        ProductCategory category,
        ProductStatus status,
        TradeType type,
        Member member
    ) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.price = price;
        this.status = status;
        this.type = type;
        this.member = member;
    }
}
