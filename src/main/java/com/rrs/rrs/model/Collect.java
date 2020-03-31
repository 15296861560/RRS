package com.rrs.rrs.model;

import lombok.Data;

@Data
public class Collect {
    private Long userId;//用户id
    private Long foodId;//食物id
    private Long gmtCreate;//收藏时间
}
