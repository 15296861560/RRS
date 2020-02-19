package com.rrs.rrs.dto;

import lombok.Data;

@Data
public class BasketDetailDTO {
    private Long basketId;//购物车id
    private Long basketDetailId;//购物车细节id
    private Integer qty;//数量
    private Long foodId;//食物id
    private String foodName;//食物名称
    private Double price;//食物单价
    private Double subtotal;//食物小计
    private String foodUrl;//食物图片地址
    private String type;//食物类型
    private String status;//食物状态


}
