package com.excel.poi.enums;

/**
 * @author Liuweiwei
 * @date 2021-07-10
 */
public enum NumericEnums {
    ZERO(0, "ZERO"),
    ONE(1, "ONE"),
    TWO(2, "TWO"),
    THREE(3, "THREE"),
    FOUR(4, "FOUR"),
    FIVE(5, "FIVE"),
    SIX(6, "SIX"),
    SEVEN(7, "SEVEN"),
    EIGHT(8, "EIGHT"),
    NINE(9, "NINE");

    private int key;
    private String value;

    NumericEnums(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
