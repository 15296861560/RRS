package com.rrs.rrs.mapper;

import com.rrs.rrs.dto.DataQueryDTO;
import com.rrs.rrs.dto.QueryDTO;
import com.rrs.rrs.model.Food;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface FoodMapper {
    //根据id寻找菜品
    @Select("select * from menu_table where food_id=#{id}")
    Food findById(@Param("id") Long id);

    //添加新菜品
    @Insert("insert into menu_table(food_id,food_name,price,food_url,type,status,gmt_create) " +
            "values(#{foodId},#{foodName},#{price},#{foodUrl},#{type},#{status},#{gmtCreate})")
    void createFood(Food food);

    //查询菜单并将查询内容加入list中
    @Select("select * from menu_table")
    List<Food>  selectAll();

    //分页查询菜单
    @Select("select * from menu_table  order by food_id limit #{offset},#{size}")
    List<Food> listAll(@Param(value = "offset") Integer offset, @Param(value = "size") Integer size);

    //查询菜品种类数量
    @Select("select count(1) from menu_table")
    Integer foodCountAll();

    //查询符合搜索条件(菜品名)的菜品的总数
    @Select("select count(*) from menu_table where food_name regexp #{search}")
    Integer searchCountByName(@Param(value = "search") String search);

    //带条件的分页查询
    @Select("select * from menu_table where food_name regexp #{name} and status regexp #{status} and type regexp #{type} order by ${condition} ${sort} limit #{offset},#{size} ")
    List<Food> listSearch(DataQueryDTO dataQueryDTO);

    //带条件的查询数目
    @Select("select count(1) from menu_table where food_name regexp #{name} and status regexp #{status} and type regexp #{type}")
    Integer listSearchCount(DataQueryDTO dataQueryDTO);

    //查询某种类型的菜品的总数
    @Select("select count(*) from menu_table where type=#{search}")
    Integer searchCountByType(String search);

    //查询某种类型的菜品并进行分页处理
    @Select("select * from menu_table where type=#{search} order by food_id limit #{offset},#{size}")
    List<Food> listSearchByType(QueryDTO queryDTO);

    //更改菜品状态
    @Update("update menu_table set status=#{status} where food_id=#{foodId}")
    void changeFoodStatus(Food food);

    //分页查询所有菜品
    @Select("select * from menu_table  order by food_id limit #{offset},#{size}")
    List<Food> list(@Param(value = "offset")Integer offset, @Param(value = "size")Integer size);

    //删除指定菜品
    @Delete("delete from menu_table where food_id=#{foodId}")
    void deleteFood(@Param(value = "foodId")Long foodId);
}
