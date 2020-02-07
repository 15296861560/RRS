package com.rrs.rrs.enums;

public enum FoodStatusEnum {
    GOOD("GOOD","上架"),
    STOCKING("STOCKING","下架");
    private String status;
    private String message;

    FoodStatusEnum(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
