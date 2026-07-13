package com.example.bidly.domain.member.dto.response;

import com.example.bidly.domain.point.entity.Point;
import com.example.bidly.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MemberResponse {

    private final String email;
    private final String name;
    private final Point point;
    private final Product product;
}
