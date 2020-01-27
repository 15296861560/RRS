package com.rrs.rrs.dto;

import lombok.Data;

@Data
public class QueryDTO {
    String search;//搜索内容
    Integer offset;//偏移量（第几页）
    Integer size;//每页大小
}
