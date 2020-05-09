package com.rrs.rrs.model;

import lombok.Data;

@Data
public class Collect {
    private Long userId;//用户id
    private Long foodId;//菜品id
    private Long gmtCreate;//收藏时间
}
