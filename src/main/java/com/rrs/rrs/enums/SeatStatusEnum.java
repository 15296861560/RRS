package com.rrs.rrs.enums;

public enum SeatStatusEnum {
    EMPTY("EMPTY","空"),
    FULL("FULL","有人");
    private String status;
    private String message;

    SeatStatusEnum(String status, String message) {
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
