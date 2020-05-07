package com.rrs.rrs.service;


import com.alibaba.fastjson.JSONObject;
import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.dto.ResultDTO;
import com.rrs.rrs.exception.CustomizeErrorCode;
import com.rrs.rrs.mapper.AdminMapper;
import com.rrs.rrs.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

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

    //删除指定管理员
    public void deleteById(String adminId) {
        adminMapper.deleteById(adminId);
    }

    //提升权限
    public void levelUp(String adminId) {
        Admin admin=adminMapper.findById(adminId);
        int curLevel=admin.getLevel();
        if (curLevel<9){
            admin.setLevel(curLevel+1);
        }
        adminMapper.changeLevel(admin);
    }

    //降低权限
    public void levelDown(String adminId) {
        Admin admin=adminMapper.findById(adminId);
        int curLevel=admin.getLevel();
        if (curLevel>1){
            admin.setLevel(curLevel-1);
        }
        adminMapper.changeLevel(admin);
    }

    //添加新管理员
    public Object addAmin(String adminId,String adminName,String phone) {
        try {
            if (adminId.length()==0||adminName.length()==0||phone.length()==0)return ResultDTO.errorOf(CustomizeErrorCode.ADD_ADMIN_FAIL);

            Admin admin=new Admin();
            admin.setAdminId(adminId);
            admin.setAdminName(adminName);
            admin.setPhone(phone);
            admin.setLevel(1);//初始权限为1
            //默认密码为111111，对默认密码进行加密
            String password= DigestUtils.md5DigestAsHex("111111".getBytes());
            admin.setPassword(password);
            adminMapper.createAdmin(admin);
                return ResultDTO.okOf();
        }catch (Exception e){
            return ResultDTO.errorOf(CustomizeErrorCode.ADD_ADMIN_FAIL);
        }

    }
}
