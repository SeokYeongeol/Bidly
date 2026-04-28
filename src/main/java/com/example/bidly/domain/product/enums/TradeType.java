package com.example.bidly.domain.product.enums;

public enum TradeType {
    AUCTION("경매"),
    DIRECT("즉시 판매");

    private final String type;

    TradeType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
