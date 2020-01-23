package com.rrs.rrs.service;


import com.rrs.rrs.mapper.AdminMapper;
import com.rrs.rrs.mapper.UserMapper;
import com.rrs.rrs.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;

    public boolean confirm(Long id,String password){
        Admin admin=adminMapper.findById(id);
        if (admin==null)return false;
        if (admin.getPassword().equals(password))return true;
        else return false;
    }


    public Admin findById(Long id){
        return adminMapper.findById(id);
    }
}
