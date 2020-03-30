package com.rrs.rrs.service;


import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.mapper.AdminMapper;
import com.rrs.rrs.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public PageDTO list(Integer page, Integer size) {
        PageDTO<Admin> pageDTO=new PageDTO();
        Integer totalCount;
        totalCount = adminMapper.adminCountAll();
        pageDTO.setPageDTO(totalCount,page,size);

        Integer offset=size*(page-1);//偏移量
        List<Admin> admins=adminMapper.listAll(offset,size);//分页

        pageDTO.setDataDTOS(admins);
        return pageDTO;
    }
}
