package com.example.bidly.domain.product.repository;

import com.example.bidly.domain.product.entity.Product;
import com.example.bidly.domain.product.enums.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.status = :status")
    Page<Product> findAllByProduct(@Param("status") ProductStatus status, Pageable pageable);
}
