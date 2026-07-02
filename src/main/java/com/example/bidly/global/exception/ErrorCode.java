package com.example.bidly.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 이메일 인증
    EMAIL_VERIFICATION_NOT_FOUND("인증 코드를 먼저 요청해주세요.", BAD_REQUEST),
    EMAIL_VERIFICATION_EXPIRED("인증 코드가 만료됐어요. 다시 요청해주세요.", BAD_REQUEST),
    EMAIL_VERIFICATION_INVALID_CODE("인증 코드가 올바르지 않아요.", BAD_REQUEST),
    EMAIL_NOT_VERIFIED("이메일 인증이 필요해요.", BAD_REQUEST),

    // 알림
    NOTIFICATION_NOT_FOUND("해당 알림을 찾을 수 없습니다.", NOT_FOUND),
    NOTIFICATION_NOT_EQUALS_USER("해당 알림을 받은 유저가 아닙니다.", FORBIDDEN),

    // 채팅
    CANNOT_CHAT_WITH_SELF("채팅 대상이 자기 자신일 수 없습니다.", BAD_REQUEST),
    CHAT_ROOM_NOT_FOUND("해당 채팅방을 찾을 수 없습니다.", NOT_FOUND),
    NOT_CHAT_PARTICIPANT("채팅 참여자가 없습니다.", BAD_REQUEST),

    // 결제
    ALREADY_PAYMENT_SUCCESS("이미 결제가 완료됐습니다.", CONFLICT),
    ALREADY_PAYMENT_FAILED("이미 결제에 실패했습니다.", CONFLICT),
    PAYMENT_NOT_SUCCESS("아직 결제가 완료되지 않았습니다.", CONFLICT),
    PAYMENT_VALID_ERROR("결제 검증중 오류가 발생했습니다.", INTERNAL_SERVER_ERROR),

    // 이미지
    S3_UPLOAD_FAILED("사진 업로드에 실패했습니다.", INTERNAL_SERVER_ERROR),

    // 포인트
    INSUFFICIENT_POINT("포인트가 충분하지 않습니다.", BAD_REQUEST),

    // 입찰
    BID_PRICE_LOWER_START_PRICE("시작 금액보다 입찰가가 낮습니다.", BAD_REQUEST),
    BID_PRICE_LOWER_CURRENT_PRICE("현재 금액보다 입찰가가 낮습니다.", BAD_REQUEST),
    BID_PRICE_DIVIDE_THOUSAND("1000원 단위로 입찰해야 합니다.", BAD_REQUEST),
    SELLER_BIDDER_DUPLICATED("자기 자신의 물건을 입찰할 수 없습니다.", CONFLICT),

    // 경매
    AUCTION_NOT_FOUND("해당 경매를 찾을 수 없습니다.", NOT_FOUND),
    AUCTION_ALREADY_CLOSED("경매가 이미 닫힌 상태입니다.", NOT_FOUND),
    CANNOT_CANCEL_AUCTION_WITH_BIDS("입찰자가 있으면 경매를 취소할 수 없습니다.", CONFLICT),

    // 상품
    PRODUCT_NOT_FOUND("해당 상품를 찾을 수 없습니다.", NOT_FOUND),
    PRODUCT_SOLD_OUT("해당 상품은 품절입니다.", NOT_FOUND),
    INPUT_START_PRICE("해당 상품의 시작 금액을 입력해주세요.", BAD_REQUEST),
    INPUT_DURATION("해당 상품의 경매 기간을 입력해주세요.", BAD_REQUEST),
    INPUT_PRICE("해당 상품의 가격을 입력해주세요.", BAD_REQUEST),
    NOT_PRODUCT_SELLER("해당 물건의 판매자가 아닙니다.", CONFLICT),

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