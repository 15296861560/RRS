package com.rrs.rrs.mapper;

import com.rrs.rrs.model.Order;
import org.apache.ibatis.annotations.*;

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

    //根据订单号删除订单
    @Delete("delete from order_table where order_id=#{orderId}")
    void deleteOrderById(@Param(value = "orderId")Long orderId);

    //更改订单状态
    @Update("update order_table set order_status=#{orderStatus} where order_id=#{seatId}")
    void changeOrderStatus(@Param(value = "orderId")Long orderId,@Param(value = "orderStatus") String orderStatus);
}
