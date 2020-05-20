package com.rrs.rrs.enums;

public enum OrderStatusEnum {
    APPLYING("APPLYING","申请预订"),
    APPLY_OK("APPLY_OK","预订成功"),
    REFUNDING("REFUNDING","申请退款"),
    REFUND_OK("REFUND_OK","退款成功"),
    FINISH("FINISH","已完成");
    private String status;
    private String message;

    OrderStatusEnum(String status, String message) {
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
