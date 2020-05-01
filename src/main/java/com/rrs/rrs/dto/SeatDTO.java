package com.rrs.rrs.dto;

import lombok.Data;

@Data
public class SeatDTO {
    private Integer seatId;//餐台id
    private String location;//餐台位置
    private String seatStatus;//餐台状态（在同一时间段，有空和有人两种情况）
    private String seatUrl;//餐台图片地址
}
