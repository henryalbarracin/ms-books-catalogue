package com.berlin.ms_books_catalogue.utils;

public final class  Consts {

    private Consts() {
        throw new IllegalStateException("Utility class");
    }

    //Nombres de campos
    public static final String AUTHOR = "author";
    public static final String CATEGORY = "category";
    public static final String DIGITAL = "digital";
    public static final String ISBN = "isbn";
    public static final String PRICE = "price";
    public static final String PUBLICATION = "publication";
    public static final String STOCK = "stock";
    public static final String TITLE = "title";
    public static final String VISIBLE = "visible";

    //Nombres de agregaciones
    public static final String TITLE_VALUES = "titlevalues";
    public static final String CATEGORY_VALUES = "CategoryValues";

    public static final String PRICE_VALUES = "priceValues";
    public static final String RANGE_PRICE_0 = "10";
    public static final String RANGE_PRICE_1 = "10-100";
    public static final String RANGE_PRICE_2 = "100";
}
