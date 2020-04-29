package com.rrs.rrs.model;

import lombok.Data;

@Data
public class Seat {
    private Integer seatId;//餐台id
    private String location;//餐台位置
    private String seatStatus;//餐台状态（有空和有人两种情况）
}
