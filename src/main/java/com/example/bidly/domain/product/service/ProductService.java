package com.example.bidly.domain.product.service;

import com.example.bidly.domain.member.entity.Member;
import com.example.bidly.domain.product.dto.request.CreateProductRequest;
import com.example.bidly.domain.product.dto.response.ProductResponse;
import com.example.bidly.domain.product.entity.Product;
import com.example.bidly.domain.product.enums.ProductStatus;
import com.example.bidly.domain.product.event.ProductCreatedEvent;
import com.example.bidly.domain.product.repository.ProductRepository;
import com.example.bidly.domain.productimage.entity.ProductImage;
import com.example.bidly.domain.productimage.repository.ProductImageRepository;
import com.example.bidly.global.entity.Auth;
import com.example.bidly.global.exception.ServerException;
import com.example.bidly.global.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.bidly.domain.product.enums.ProductStatus.ON_SALE;
import static com.example.bidly.domain.product.enums.ProductStatus.SOLD_OUT;
import static com.example.bidly.domain.product.enums.TradeType.AUCTION;
import static com.example.bidly.domain.product.enums.TradeType.DIRECT;
import static com.example.bidly.global.exception.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ProductImageRepository productImageRepository;
    private final S3Service s3Service;

    @Transactional
    public ProductResponse createProduct(Auth auth, CreateProductRequest request, List<MultipartFile> images) {
        Member findMember = Member.fromAuth(auth.getId());
        if (request.getTradeType().equals(DIRECT)) {
            if (request.getPrice() == null) throw new ServerException(INPUT_PRICE);
        }
        else if (request.getTradeType().equals(AUCTION)) {
            if (request.getStartPrice() == null) throw new ServerException(INPUT_START_PRICE);
            if (request.getDuration() == null) throw new ServerException(INPUT_DURATION);
        }

        Product savedProduct = Product.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .price(request.getTradeType().equals(DIRECT) ? request.getPrice() : null)
                .category(request.getCategory())
                .status(ON_SALE)
                .type(request.getTradeType())
                .seller(findMember)
                .build();
        productRepository.save(savedProduct);

        for (int i = 0; i < images.size(); i++) {
            String imageUrl = s3Service.upload(images.get(i), "products");
            ProductImage savedProductImage = ProductImage.builder()
                    .imageUrl(imageUrl)
                    .displayOrder(i + 1)
                    .product(savedProduct)
                    .build();
            productImageRepository.save(savedProductImage);
        }

        if (request.getTradeType().equals(AUCTION)) {
            eventPublisher.publishEvent(
                    new ProductCreatedEvent(savedProduct, request.getStartPrice(), request.getDuration())
            );
        }
        return ProductResponse.of(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponse findProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ServerException(PRODUCT_NOT_FOUND));

        if (product.getStatus().equals(SOLD_OUT)) {
            throw new ServerException(PRODUCT_SOLD_OUT);
        }
        return ProductResponse.of(product);
    }

    @Transactional(readOnly = true)
    public PagedModel<ProductResponse> findAllProducts(int page) {
        Pageable pageable = PageRequest.of(page - 1, 10,
                Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Product> products = productRepository.findAllByProduct(ON_SALE, pageable);
        return new PagedModel<>(products.map(ProductResponse::of));
    }
}
