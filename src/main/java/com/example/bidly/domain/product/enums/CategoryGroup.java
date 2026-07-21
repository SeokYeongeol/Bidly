package com.example.bidly.domain.product.enums;


import java.util.List;

import static com.example.bidly.domain.product.enums.ProductCategory.*;

public enum CategoryGroup {

    ELECTRONICS(List.of(LAPTOP, SMARTPHONE, TABLET, CAMERA, AUDIO, GAME, ELECTRONICS_ETC)),
    FASHION(List.of(SHOES, BAG, ACCESSORY, TOP, BOTTOM, OUTER, FASHION_ETC)),
    INTERIOR(List.of(FURNITURE, LIGHTING, BEDDING, INTERIOR_ETC)),
    SPORTS(List.of(BICYCLE, CAMPING, FITNESS, SPORTS_ETC)),
    BOOK_STATIONERY(List.of(BOOK, STATIONERY)),
    HOBBY(List.of(FIGURE, MUSIC_INSTRUMENT, TRADING_CARD, HOBBY_ETC)),
    ETC(List.of(ProductCategory.ETC));

    private final List<ProductCategory> categories;

    CategoryGroup(List<ProductCategory> categories) {
        this.categories = categories;
    }

    public List<ProductCategory> getCategories() {
        return categories;
    }
}
