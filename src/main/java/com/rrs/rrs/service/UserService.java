package com.rrs.rrs.service;


import com.rrs.rrs.exception.CustomizeErrorCode;
import com.rrs.rrs.mapper.UserMapper;
import com.rrs.rrs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    //注册
    public String register(String phone,String userName,String password){
        //检查是否已注册
        if(findByPhone(phone)!=null)return CustomizeErrorCode.REGISTER_FAIL_PHONE_REGISTERED.getMessage();

        //对密码进行加密
        String newPassword= DigestUtils.md5DigestAsHex(password.getBytes());
        //生成随机的token
        String token = UUID.randomUUID().toString();
        //如果该号码未注册过则创建用户
        User user=new User();
        user.setUserName(userName);
        user.setPhone(phone);
        user.setPassword(newPassword);
        user.setGmtCreate(System.currentTimeMillis());
        user.setGmtModified(System.currentTimeMillis());
        user.setToken(token);
        userMapper.createUser(user);
       return "success";

    }

    //校验手机号和密码
    public boolean confirm(String phone,String password){
        User user=findByPhone(phone);
        if (user==null)return false;
        //对密码进行MD5转换
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        if (user.getPassword().equals(password))return true;
        else return false;
    }



    //查找用户

    public User findById(Long id){
        return userMapper.findById(id);
    }

    public User findByPhone(String phone) {return userMapper.findByPhone(phone);}


    //删除用户

    public void deleteById(Long userId) {
        userMapper.deleteById(userId);
    }



}
