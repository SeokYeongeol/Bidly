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

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p " +
            "from Product p " +
            "where p.status = :status " +
            "and (:category is null or p.category = :category) " +
            "and (:tradeType is null or p.type = : tradeType) " +
            "and (:keyword is null " +
                "or p.title like %:keyword% " +
                "or p.description like %:keyword%" +
            ")"
    )
    Page<Product> productSearch(
            @Param("category") ProductCategory category,
            @Param("tradeType") TradeType tradeType,
            @Param("status") ProductStatus status,
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
