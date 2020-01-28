package com.rrs.rrs.service;


import com.rrs.rrs.mapper.UserMapper;
import com.rrs.rrs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public String register(Long id,String password){
        //检查是否已注册
       return null;

    }

    //校验手机号和密码
    public boolean confirm(String phone,String password){
        User user=userMapper.findByPhone(phone);
        if (user==null)return false;
        //对密码进行MD5转换
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        if (user.getPassword().equals(password))return true;
        else return false;
    }


    //    创建用户
    private void createUser(User user,String password){

        //加密后的密码
        String newPassword= DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(newPassword);
        userMapper.createUser(user);

    }

    public User findById(Long id){
        return userMapper.findById(id);
    }

    public User findByPhone(String phone) {return userMapper.findByPhone(phone);}



    public void deleteById(Long userId) {
        userMapper.deleteById(userId);
    }

    public void bindingPhone(User user, String phone) {//绑定手机号
        user.setPhone(phone);
        user.setGmtModified(System.currentTimeMillis());
        userMapper.update(user);
    }


}
