package com.rrs.rrs.dto;

import lombok.Data;

@Data
public class HistoryDTO {
    private Long buyTime;//下单时间
    private Double amount;//订单总额
    private String orderTime;//预约时间
    private String content;//订单内容
    private String orderStatus;//订单状态
    private String location;//预订餐台
    private Long basketId;//订单详情id
}
