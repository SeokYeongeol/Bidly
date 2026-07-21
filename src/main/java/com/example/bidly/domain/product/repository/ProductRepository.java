package com.example.bidly.domain.product.repository;

import com.example.bidly.domain.product.entity.Product;
import com.example.bidly.domain.product.enums.ProductCategory;
import com.example.bidly.domain.product.enums.ProductStatus;
import com.example.bidly.domain.product.enums.TradeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p " +
            "from Product p " +
            "where p.status = :status " +
            "and (:#{#categories == null || #categories.isEmpty()} = true " +
                "or p.category in :categories) " +
            "and (:tradeType is null or p.type = : tradeType) " +
            "and (:keyword is null " +
                "or p.title like %:keyword% " +
                "or p.description like %:keyword%" +
            ")"
    )
    Page<Product> productSearch(
            @Param("categories") List<ProductCategory> categories,
            @Param("tradeType") TradeType tradeType,
            @Param("status") ProductStatus status,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("select p from Product p where p.seller.id = :sellerId")
    Page<Product> findMyProducts(@Param("sellerId") Long sellerId, Pageable pageable);
}
