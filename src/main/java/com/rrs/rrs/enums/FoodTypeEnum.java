package com.rrs.rrs.enums;

public enum FoodTypeEnum {
    A("A","炒菜"),
    D("D","饮料");


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
