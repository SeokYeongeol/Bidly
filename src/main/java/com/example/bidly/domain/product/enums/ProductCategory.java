package com.example.bidly.domain.product.enums;

public enum ProductCategory {

    // 전자기기
    LAPTOP("노트북"),
    SMARTPHONE("스마트폰"),
    TABLET("태블릿"),
    CAMERA("카메라"),
    AUDIO("음향기기"),
    GAME("게임/콘솔"),
    ELECTRONICS_ETC("전자기기 기타"),

    // 의류/패션
    TOP("상의"),
    BOTTOM("하의"),
    OUTER("아우터"),
    SHOES("신발"),
    BAG("가방"),
    ACCESSORY("액세서리"),
    FASHION_ETC("의류/패션 기타"),

    // 가구/인테리어
    FURNITURE("가구"),
    LIGHTING("조명"),
    BEDDING("침구"),
    INTERIOR_ETC("가구/인테리어 기타"),

    // 스포츠/레저
    BICYCLE("자전거"),
    CAMPING("캠핑"),
    FITNESS("헬스/피트니스"),
    SPORTS_ETC("스포츠/레저 기타"),

    // 도서/문구
    BOOK("도서"),
    STATIONERY("문구"),

    // 취미/수집
    FIGURE("피규어"),
    MUSIC_INSTRUMENT("악기"),
    TRADING_CARD("트레이딩카드"),
    HOBBY_ETC("취미/수집 기타"),

    // 기타
    ETC("기타");


    private final String displayName;

    ProductCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
