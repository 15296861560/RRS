package com.rrs.rrs.model;

import lombok.Data;

@Data
public class Food {
    private Long foodId;//食物id
    private String foodName;//食物名称
    private Double price;//食物价格
    private String foodUrl;//食物图片地址
    private String type;//食物类型
    private String status;//食物当前是否还有库存
    private Long gmtCreate;//上架时间
}
