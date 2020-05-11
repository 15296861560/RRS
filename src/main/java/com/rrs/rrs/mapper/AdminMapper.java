package com.rrs.rrs.mapper;

import com.rrs.rrs.model.Admin;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface AdminMapper {
    @Select("select * from admin_table where admin_id=#{adminId}")
    Admin findById(@Param("adminId") String adminId);

    @Select("select count(1) from admin_table")
    Integer adminCountAll();

    @Select("select * from admin_table limit #{offset},#{size}")
    List<Admin> listAll(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Delete("delete from admin_table where admin_id=#{adminId}")
    void deleteById(@Param(value = "adminId") String adminId);

    //更改权限
    @Update("update admin_table set level=#{level} where admin_id=#{adminId}")
    void changeLevel(Admin admin);

    //更改密码
    @Update("update admin_table set password=#{password} where admin_id=#{adminId}")
    void changePassword(Admin admin);

    //创建用户
    @Insert("insert into admin_table(admin_id,admin_name,level,password,phone) " +
            "values(#{adminId},#{adminName},#{level},#{password},#{phone})")
    void createAdmin(Admin admin);
}
