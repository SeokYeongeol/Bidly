package com.example.bidly.domain.product.entity;

import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.product.enums.ProductCategory;
import com.example.bidly.domain.product.enums.ProductStatus;
import com.example.bidly.domain.product.enums.TradeType;
import com.example.bidly.domain.productimage.entity.ProductImage;
import com.example.bidly.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private Long price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Member seller;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    private List<ProductImage> images = new ArrayList<>();

    @Builder
    public Product(
        String title,
        String description,
        Long price,
        ProductCategory category,
        ProductStatus status,
        TradeType type,
        Member seller
    ) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.price = price;
        this.status = status;
        this.type = type;
        this.seller = seller;
    }

    public void updateStatus(ProductStatus status) {
        this.status = status;
    }

    public boolean isSeller(Long sellerId) { return this.seller.getId().equals(sellerId); }

    public void delete() { this.status = ProductStatus.DELETED; }
}
