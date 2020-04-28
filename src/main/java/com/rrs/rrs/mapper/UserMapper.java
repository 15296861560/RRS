package com.rrs.rrs.mapper;

import com.rrs.rrs.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UserMapper {
    //根据id查用户
    @Select("select * from user_table where user_id=#{userId}")
    User findById(@Param("userId") Long userId);

    //根据手机号码查用户
    @Select("select * from user_table where phone=#{phone}")
    User findByPhone(@Param("phone") String phone);

    //创建用户
    @Insert("insert into user_table(user_name,gmt_create,gmt_modified,token,password,phone,code) " +
            "values(#{userName},#{gmtCreate},#{gmtModified},#{token},#{password},#{phone},#{code})")
    void createUser(User user);

    //根据token查询用户
    @Select("select * from user_table where token=#{token}")
    User findByToken(@Param("token") String token);

    //查询所有用户数量
    @Select("select count(1) from user_table")
    Integer userCountAll();

    // 查询所有用户并进行分页处理
    @Select("select * from user_table limit #{offset},#{size}")
    List<User> listAll(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    //更新用户数据
    @Update("update user_table set user_name=#{userName},gmt_modified=#{gmtModified}," +
            "password=#{password},phone=#{phone},code=#{code} where user_id=#{userId}")
    void update(User user);

    //根据id删除用户
    @Delete("delete from user_table where id=#{userId}")
    void deleteById(@Param(value = "userId") Long userId);

    //模糊查询用户姓名包涵某字段的用户数量
    @Select("select count(1) from user_table where user_name regexp #{search}")
    Integer userCountBySearchName(@Param(value = "search") String search);

    //模糊查询用户姓名包涵某字段的用户并进行分页处理
    @Select("select * from user_table where user_name=#{name}")
    List<User> listSearchByName( @Param(value = "name") String name);

    //模糊查询用户姓名包涵某字段的用户并进行分页处理
    @Select("select * from user_table where user_name regexp #{search} limit #{offset},#{size}")
    List<User> listBySearch(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size, @Param(value = "search") String search);


    //根据编码查询用户
    @Select("select * from user_table where code=#{code}")
    User findByCode(@Param("code") String code);

}
