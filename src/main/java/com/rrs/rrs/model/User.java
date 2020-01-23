package com.rrs.rrs.model;

import lombok.Data;

@Data
public class User {
    private Long userId;//用户id
    private String userName;//用户姓名
    private Long gmtCreate;//注册时间
    private Long gmtModified;//修改资料时间
    private String token;//身份标识
    private String password;//用户密码
    private String phone;//手机号码
}
