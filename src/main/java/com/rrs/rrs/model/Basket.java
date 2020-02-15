package com.rrs.rrs.model;

import lombok.Data;

@Data
public class Basket {
    private Long basketId;//购物车id
    private Long gmtCreate;//创建时间
    private Long gmtModified;//修改时间
    private Long userId;//用户id
    private String basketStatus;//购物车状态，当用户第一次加入东西到购物车后为treu，结算后为false
    private Double payment;//支付金额

}
