package com.rrs.rrs.service;


import com.rrs.rrs.mapper.UserMapper;
import com.rrs.rrs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public String register(Long id,String password){
        //检查是否已注册
       return null;

    }

    public boolean confirm(String phone,String password){
        User user=userMapper.findByPhone(phone);
        if (user==null)return false;
        if (user.getPassword().equals(password))return true;
        else return false;
    }


    //    创建用户
    private void createUser(User user,String password) {

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
