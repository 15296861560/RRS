package com.rrs.rrs.mapper;

import com.rrs.rrs.model.BasketDetail;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface BasketDetailMapper {
    @Select("select * from basket_detail_table where basket_detail_id=#{basketDetailId}")
    BasketDetail findById(@Param("basketDetailId") Long basketDetailId);


    @Insert("insert into basket_detail_table(basket_id,qty,food_id)values(#{basketId},#{qty},#{foodId})")
    void createBasketDetail(BasketDetail basketDetail);

    //根据BasketDetailId删除某条数据
    @Delete("delete from basket_detail_table where basket_detail_id=#{basketDetailId}")
    void deleteBasketDetailById(@Param(value = "basketDetailId") Long basketDetailId);

    //根据BasketId删除某部分数据
    @Delete("delete from basket_detail_table where basket_id=#{basketId}")
    void deleteBasketDetailByBasketId(@Param(value = "basketId") Long basketId);

    @Select("select * from basket_detail_table where basket_id=#{basketId}")
    List<BasketDetail> findByBasketId(@Param("basketId") Long basketId);

    //重新修改某个食物的数量
    @Update("update basket_detail_table set qty=#{qty} where basket_detail_id=#{basketDetailId}")
    void changeBasketDetailQty(BasketDetail basketDetail);
}
