package com.rrs.rrs.model;

import lombok.Data;

@Data
public class Order {
    private Long orderId;//订单id
    private Long userId;//预订人id
    private Long basketId;//购物车id（可查订单内容）
    private Integer seatId;//预订餐台id
    private String orderTime;//预订时间id
    private String content;//订单内容
    private Double amount;//订单总额
    private String orderStatus;//订单状态

}
