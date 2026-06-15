package com.example.bidly.domain.product.controller;

import com.example.bidly.domain.product.dto.request.CreateProductRequest;
import com.example.bidly.domain.product.dto.response.ProductResponse;
import com.example.bidly.domain.product.service.ProductService;
import com.example.bidly.global.entity.Auth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    @PostMapping(value = "/v1/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductResponse> createProduct(
            @AuthenticationPrincipal Auth auth,
            @Valid @RequestPart CreateProductRequest request,
            @RequestPart(required = false)List<MultipartFile> images
    ) {
        return ResponseEntity.ok(productService.createProduct(auth, request, images));
    }

    @GetMapping("/v1/products/{productId}")
    public ResponseEntity<ProductResponse> findProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.findProduct(productId));
    }

    @GetMapping("/v1/products")
    public ResponseEntity<PagedModel<ProductResponse>> findAllProducts(@RequestParam int page) {
        return ResponseEntity.ok(productService.findAllProducts(page));
    }
}
