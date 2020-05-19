package com.rrs.rrs.mapper;

import com.rrs.rrs.model.Advise;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface AdviseMapper {

    //根据id查找建议
    @Select("select * from advise_table where advise_id=#{adviseId}")
    Advise findById(@Param("adviseId") Long adviseId);

    //分页查询所有建议
    @Select("select * from advise_table limit #{offset},#{size}")
    List<Advise> listAll(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    //创建建议
    @Insert("insert into advise_table(title,description,gmt_create,advise_type,creator) " +
            "values(#{title},#{description},#{gmtCreate},#{adviseType},#{creator})")
    void createAdvise(Advise advise);

    //分页查询所有建议和投诉总数
    @Select("select count(1) from advise_table")
    Integer getAllCount();


}
