package com.rrs.rrs.dto;

import lombok.Data;

@Data
public class FoodDTO {
    private Long foodId;//食物id
    private String foodName;//食物名称
    private Double price;//食物价格
    private String foodUrl;//食物图片地址
    private String type;//食物类型
    private String status;//食物状态
    private Long likeCount;//点赞数
}
