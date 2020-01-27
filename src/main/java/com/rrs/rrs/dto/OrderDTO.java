package com.rrs.rrs.dto;

import lombok.Data;

@Data
public class OrderDTO {
    private String userName;//预订人姓名
    private String location;//预订座位位置
    private Long gmtOrder;//预订时间
    private String content;//订单内容
    private String phone;//预订人联系号码
    private Double amount;//订单总额
    private String orderStatus;//订单状态

}
