package com.example.bidly.domain.notification.enums;

public enum NotificationType {
    BID_RECEIVED,    // 입찰 당함
    BID_FAILED,      // 입찰 실패 (더 높은 입찰가 들어옴)
    AUCTION_WON,     // 낙찰
    AUCTION_CLOSED,  // 경매 마감
    CHAT_RECEIVED    // 채팅 수신
}