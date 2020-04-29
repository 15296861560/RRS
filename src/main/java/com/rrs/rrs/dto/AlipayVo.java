package com.rrs.rrs.dto;

import lombok.Data;

@Data
public class AlipayVo {

    private String  out_trade_no;// 订单的id
    private String total_amount;//支付金额
    private String subject;//产品名称
    private String product_code;//产品码

}
