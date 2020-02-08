package com.rrs.rrs.mapper;

import com.rrs.rrs.dto.FoodQueryDTO;
import com.rrs.rrs.dto.QueryDTO;
import com.rrs.rrs.model.Food;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface FoodMapper {
    //根据id寻找食物
    @Select("select * from menu_table where food_id=#{id}")
    Food findById(@Param("id") Long id);

    //添加新食物
    @Insert("insert into menu_table(food_id,food_name,price,food_url,type,status) " +
            "values(#{foodId},#{foodName},#{price},#{foodUrl},#{type},#{status})")
    void createFood(Food food);

    //查询菜单并将查询内容加入list中
    @Select("select * from menu_table")
    List<Food>  selectAll();

    //分页查询菜单
    @Select("select * from menu_table  order by food_id limit #{offset},#{size}")
    List<Food> listAll(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    //查询食物种类数量
    @Select("select count(1) from menu_table")
    Integer foodCountAll();

    //查询符合搜索条件(食物名)的食物的总数
    @Select("select count(*) from menu_table where food_name regexp #{search}")
    Integer searchCountByName(@Param(value = "search") String search);

    //带条件的分页查询
    @Select("select * from menu_table where food_name regexp #{name} and status regexp #{status} and type regexp #{type} order by food_id limit #{offset},#{size}")
    List<Food> listSearch(FoodQueryDTO foodQueryDTO);

    //查询某种类型的食物的总数
    @Select("select count(*) from menu_table where type=#{search}")
    Integer searchCountByType(String search);

    //查询某种类型的食物并进行分页处理
    @Select("select * from menu_table where type=#{search} order by food_id limit #{offset},#{size}")
    List<Food> listSearchByType(QueryDTO queryDTO);

    //更改食物状态
    @Update("update menu_table set status=#{status} where food_id=#{foodId}")
    void changeFoodStatus(Food food);

    //分页查询所有食物
    @Select("select * from menu_table  order by food_id limit #{offset},#{size}")
    List<Food> list(@Param(value = "offset")Integer offset, @Param(value = "size")Integer size);

    //删除指定食物
    @Delete("delete from menu_table where food_id=#{foodId}")
    void deleteFood(@Param(value = "foodId")Long foodId);
}
