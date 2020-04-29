package com.rrs.rrs.dto;

import lombok.Data;

@Data
public class AlipayVo {
    private String  out_trade_no;
    private String total_amount;
    private String subject;
    private String product_code;

}
