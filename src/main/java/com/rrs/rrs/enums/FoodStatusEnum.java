package com.rrs.rrs.enums;

public enum FoodStatusEnum {
    GOOD("","库存充足"),
    STOCKING("Stocking","进货中");
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
