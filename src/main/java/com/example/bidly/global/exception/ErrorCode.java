package com.example.bidly.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 입찰
    BID_PRICE_LOWER_START_PRICE("시작 금액보다 입찰가가 낮습니다.", BAD_REQUEST),
    BID_PRICE_LOWER_CURRENT_PRICE("현재 금액보다 입찰가가 낮습니다.", BAD_REQUEST),
    BID_PRICE_DIVIDE_TEN_THOUSAND("10000원 단위로 입찰해야 합니다.", BAD_REQUEST),
    SELLER_BIDDER_DUPLICATED("자기 자신의 물건을 입찰할 수 없습니다.", CONFLICT),

    // 경매
    AUCTION_NOT_FOUND("해당 경매를 찾을 수 없습니다.", NOT_FOUND),

    // 상품
    PRODUCT_NOT_FOUND("해당 상품를 찾을 수 없습니다.", NOT_FOUND),
    PRODUCT_SOLD_OUT("해당 상품은 품절입니다.", NOT_FOUND),
    INPUT_START_PRICE("해당 상품의 시작 금액을 입력해주세요.", BAD_REQUEST),
    INPUT_DURATION("해당 상품의 경매 기간을 입력해주세요.", BAD_REQUEST),
    INPUT_PRICE("해당 상품의 가격을 입력해주세요.", BAD_REQUEST),

    // 유저
    USER_NOT_FOUND("해당 사람을 찾을 수 없습니다.", NOT_FOUND),
    USER_EMAIL_DUPLICATION("다른 사람과 이메일이 중복됩니다.", CONFLICT),
    USER_NAME_DUPLICATION("다른 사람과 닉네임이 중복됩니다.", CONFLICT),
    INVALID_TOKEN("유효하지 않은 토큰입니다.", INTERNAL_SERVER_ERROR),
    INVALID_JWT("유효하지 않는 JWT 서명입니다.", UNAUTHORIZED),
    EXPIRED_JWT("만료된 JWT 토큰입니다.", UNAUTHORIZED),
    UNSUPPORTED_JWT("지원되지 않는 JWT 토큰입니다.", BAD_REQUEST),
    INVALID_USER_ROLE("유효하지 않는 역할입니다.", BAD_REQUEST),
    PASSWORD_SAME_AS_OLD("이전 패스워드와 동일할 수 없습니다.", BAD_REQUEST),
    INVALID_PASSWORD("비밀번호가 일치하지 않습니다.", BAD_REQUEST);


    private final String message;
    private final HttpStatus status;
}