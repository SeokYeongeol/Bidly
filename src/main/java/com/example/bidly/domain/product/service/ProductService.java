package com.example.bidly.domain.product.service;

import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.product.dto.request.CreateProductRequest;
import com.example.bidly.domain.product.dto.response.ProductResponse;
import com.example.bidly.domain.product.entity.Product;
import com.example.bidly.domain.product.enums.ProductStatus;
import com.example.bidly.domain.product.event.ProductCreatedEvent;
import com.example.bidly.domain.product.repository.ProductRepository;
import com.example.bidly.global.entity.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.bidly.domain.product.enums.TradeType.AUCTION;
import static com.example.bidly.domain.product.enums.TradeType.DIRECT;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public ProductResponse createProduct(Auth auth, CreateProductRequest request) {
        Member findMember = Member.fromAuth(auth.getId());

        Product savedProduct = Product.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getTradeType().equals(DIRECT) ? request.getPrice() : null)
                .category(request.getCategory())
                .status(ProductStatus.ON_SALE)
                .type(request.getTradeType())
                .seller(findMember)
                .build();
        productRepository.save(savedProduct);

        if (request.getTradeType().equals(AUCTION)) {
            eventPublisher.publishEvent(
                    new ProductCreatedEvent(savedProduct, request.getStartPrice(), request.getDuration())
            );
        }
        return ProductResponse.of(savedProduct);
    }
}
