package com.example.bidly.domain.product.controller;

import com.example.bidly.domain.product.dto.request.CreateProductRequest;
import com.example.bidly.domain.product.dto.response.ProductResponse;
import com.example.bidly.domain.product.service.ProductService;
import com.example.bidly.global.entity.Auth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/v1/products")
    public ResponseEntity<ProductResponse> createProduct(
        @AuthenticationPrincipal Auth auth,
        @Valid @RequestBody CreateProductRequest request
    )
    {
        return ResponseEntity.ok(productService.createProduct(auth, request));
    }
}
