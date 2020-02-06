package com.rrs.rrs.dto;

import lombok.Data;

@Data
public class FoodQueryDTO {
    Integer offset;//偏移量（第几页）
    Integer size;//每页大小
    String name;//名称
    String status;//状态
    String type;//类型

}
