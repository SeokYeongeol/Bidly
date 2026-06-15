package com.example.bidly.domain.productimage.entity;

import com.example.bidly.domain.product.entity.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Integer displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Builder
    public ProductImage(String imageUrl, Integer displayOrder, Product product) {
        this.imageUrl = imageUrl;
        this.displayOrder = displayOrder;
        this.product = product;
    }

    public boolean isThumbnail() {
        return this.displayOrder == 1;
    }
}
