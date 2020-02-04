package com.rrs.rrs.service;


import com.rrs.rrs.mapper.AdminMapper;
import com.rrs.rrs.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;

    public boolean confirm(String id,String password){
        Admin admin=findById(id);
        if (admin==null)return false;
        if (admin.getPassword().equals(password))return true;
        else return false;
    }


    public Admin findById(String id){
        return adminMapper.findById(id);
    }
}
