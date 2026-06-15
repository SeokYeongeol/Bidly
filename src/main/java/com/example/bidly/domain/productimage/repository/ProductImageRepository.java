package com.example.bidly.domain.productimage.repository;

import com.example.bidly.domain.productimage.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
