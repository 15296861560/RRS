package com.rrs.rrs.mapper;

import com.rrs.rrs.model.Basket;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;


@Mapper
public interface BasketMapper {
    @Select("select * from basket_table where basket_id=#{basketId}")
    Basket findById(@Param("basketId") Long basketId);

    //获取某用户当前正在使用的购物车
    @Select("select * from basket_table where user_id=#{userId} and basket_status='true'")
    Basket findByUserId(@Param("userId") Long userId);

    //获取某用户的历史购物车
    @Select("select * from basket_table where user_id=#{userId}")
    ArrayList<Basket> findBasketsByUserId(@Param("userId") Long userId);

    //更改购物车状态
    @Update("update basket_table set basket_status=#{basketStatus} where basket_id=#{basketId}")
    void changeBasketStatus(@Param("basketId") Long basketId, @Param("basketStatus") String basketStatus);

    //更改购物车修改时间
    @Update("update basket_table set gmt_modified=#{gmtModified} where basket_id=#{basketId}")
    void changeBasketGmtModified(@Param("basketId") Long basketId, @Param("gmtModified") Long gmtModified);

    @Insert("insert into basket_table(gmt_create,gmt_modified,user_id,basket_status,payment)values(#{gmtCreate},#{gmtModified},#{userId},#{basketStatus},#{payment})")
    void createBasket(Basket basket);

    //根据basketId删除basket
    @Delete("delete from basket_table where basket_id=#{basketId}")
    void deleteBasket(@Param(value = "basketId") Long basketId);

    //更改支付金额
    @Update("update basket_table set payment=#{payment} where basket_id=#{basketId}")
    void changePayment(Basket basket);

    //获取所有状态为true的购物车id
    @Select("select basket_id from basket_table where basket_status='true'")
    ArrayList<Long> getAllBasketStatusTrue();
}
