package com.rrs.rrs.model;

import lombok.Data;

@Data
public class Food {
    private Long foodId;//菜品id
    private String foodName;//菜品名称
    private Double price;//菜品价格
    private String foodUrl;//菜品图片地址
    private String type;//菜品类型
    private String status;//菜品当前是否还有库存
    private Long gmtCreate;//上架时间
}
