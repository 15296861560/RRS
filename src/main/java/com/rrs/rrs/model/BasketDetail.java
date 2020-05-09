package com.rrs.rrs.model;

import lombok.Data;

@Data
public class BasketDetail {
    private Long basketDetailId;//购物车细节id
    private Long basketId;//购物车id
    private Integer qty;//数量
    private Long foodId;//菜品id
}
