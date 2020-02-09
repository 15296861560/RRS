package com.rrs.rrs.enums;

public enum FoodTypeEnum {
    A("A","小炒"),
    B("B","蒸菜"),
    C("C","凉菜"),
    D("D","甜点"),
    E("E","汤类"),
    F("F","主食"),
    G("G","海鲜"),
    H("H","美酒"),
    I("I","饮料");


    private String type;
    private String message;

    FoodTypeEnum(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
