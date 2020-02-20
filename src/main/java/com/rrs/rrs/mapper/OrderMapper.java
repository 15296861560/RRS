package com.rrs.rrs.mapper;

import com.rrs.rrs.model.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface OrderMapper {

    //创建订单
    @Insert("insert into order_table(user_id,seat_id,order_time,content,amount,order_status) " +
            "values(#{userId},#{seatId},#{orderTime},#{content},#{amount},#{orderStatus})")
    void createOrder(Order order);

    //根据订单号查找订单
    @Select("select * from order_table where order_id=#{orderId}")
    Order findById(@Param("orderId") Long orderId);

    //根据用户id查找订单
    @Select("select * from order_table where user_id=#{userId}")
    List<Order> selectByUserId(@Param("userId") Long userId);

    //所有订单的总数
    @Select("select count(1) from order_table")
    Integer orderCount();

    //对所有订单进行分页处理
    @Select("select * from order_table order by order_time limit #{offset},#{size}")
    List<Order> list(@Param(value = "offset") Integer offset, @Param(value = "size")Integer size);
}
