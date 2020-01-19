package com.rrs.rrs.model;

import lombok.Data;

@Data
public class Admin {
    private Long adminId;//管理员id
    private String password;//管理员密码
    private String adminName;//管理员名称
    private Integer level;//管理员权限
}
