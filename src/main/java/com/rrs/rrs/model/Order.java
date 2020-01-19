package com.rrs.rrs.model;

import lombok.Data;

@Data
public class Order {
    private Long orderId;//订单id
    private Long userId;//预订人id
    private Integer seatId;//预订座位id
    private Long gmtOrder;//预订时间id
    private String content;//订单内容
    private Double amount;//订单总额
    private String orderStatus;//订单状态

}
