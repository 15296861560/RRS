package com.rrs.rrs.mapper;

import com.rrs.rrs.model.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface AdminMapper {
    @Select("select * from admin_table where admin_id=#{adminId}")
    Admin findById(@Param("adminId") String adminId);
}
