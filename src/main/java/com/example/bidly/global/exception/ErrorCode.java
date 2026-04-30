package com.example.bidly.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 경매
    AUCTION_NOT_FOUND("해당 경매를 찾을 수 없습니다.", NOT_FOUND),

    // 상품
    PRODUCT_NOT_FOUND("해당 상품를 찾을 수 없습니다.", NOT_FOUND),
    PRODUCT_SOLD_OUT("해당 상품은 품절입니다.", NOT_FOUND),

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