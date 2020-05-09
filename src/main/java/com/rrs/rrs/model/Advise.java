package com.rrs.rrs.model;

import lombok.Data;

@Data
public class Advise {
    private Long adviseId;//id
    private String title;//标题
    private String description;//描述
    private Long gmtCreate;//创建时间
    private String adviceType;//类型
    private Long creator;//创建者
}
