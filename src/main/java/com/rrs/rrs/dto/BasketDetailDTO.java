package com.rrs.rrs.dto;

import lombok.Data;

@Data
public class BasketDetailDTO {
    private Long basketId;//购物车id
    private Long basketDetailId;//购物车细节id
    private Integer qty;//数量
    private Long foodId;//菜品id
    private String foodName;//菜品名称
    private Double price;//菜品单价
    private Double subtotal;//菜品小计
    private String foodUrl;//菜品图片地址
    private String type;//菜品类型
    private String status;//菜品状态


}
