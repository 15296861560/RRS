package com.rrs.rrs.mapper;

import com.rrs.rrs.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UserMapper {
    @Select("select * from user_table where user_id=#{userId}")
    User findById(@Param("userId") Long userId);

    @Select("select * from user_table where phone=#{phone}")
    User findByPhone(@Param("phone") String phone);

    @Insert("insert into user_table(user_name,gmt_create,gmt_modified,token,password,phone) " +
            "values(#{userName},#{gmtCreate},#{gmtModified},#{token},#{password},#{phone})")
    void createUser(User user);

    @Select("select * from user_table where token=#{token}")
    User findByToken(@Param("token") String token);

    @Select("select count(1) from user_table")
    Integer userCountAll();

    @Select("select * from user_table limit #{offset},#{size}")
    List<User> listAll(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    @Update("update user_table set user_name=#{userName},gmt_modified=#{gmtModified}," +
            "password=#{password},phone=#{phone} where user_id=#{userId}")
    void update(User user);

    @Delete("delete from user_table where id=#{userId}")
    void deleteById(@Param(value = "userId") Long userId);

    @Select("select count(1) from user_table where name regexp #{search}")
    Integer userCountBySearchName(@Param(value = "search") String search);

    @Select("select * from user_table where name regexp #{search} limit #{offset},#{size}")
    List<User> listBySearch(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size, @Param(value = "search") String search);
}
