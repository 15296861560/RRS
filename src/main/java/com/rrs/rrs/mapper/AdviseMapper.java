package com.rrs.rrs.mapper;

import com.rrs.rrs.model.Advise;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface AdviseMapper {

    //根据id查找建议
    @Select("select * from advise_table where advise_id=#{adviseId}")
    Advise findById(@Param("adviseId") String adviseId);

    //分页查询所有建议
    @Select("select * from advise_table limit #{offset},#{size}")
    List<Advise> listAll(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    //创建建议
    @Insert("insert into advise_table(title,description,gmt_create,advice_type,creator) " +
            "values(#{title},#{description},#{gmtCreate},#{adviceType},#{creator})")
    void createAdvise(Advise advise);
}
