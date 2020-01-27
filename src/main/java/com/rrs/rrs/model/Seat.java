package com.rrs.rrs.model;

import lombok.Data;

@Data
public class Seat {
    private Integer seatId;//座位id
    private String location;//座位位置
    private String seatStatus;//座位状态
}
