package com.rrs.rrs.service;


import com.rrs.rrs.dto.OrderDTO;
import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.enums.OrderStatusEnum;
import com.rrs.rrs.mapper.OrderMapper;
import com.rrs.rrs.mapper.SeatMapper;
import com.rrs.rrs.mapper.UserMapper;
import com.rrs.rrs.model.Order;
import com.rrs.rrs.model.Seat;
import com.rrs.rrs.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SeatMapper seatMapper;



    //寻找订单
    //根据订单号
    public Order findOrderById(Long orderId){ return orderMapper.findById(orderId);}
    //根据手机号
    public List<Order> findOrderByPhone(String phone)
    {
        User user=userMapper.findByPhone(phone);
        List<Order>  orders= orderMapper.selectByUserId(user.getUserId());
        return orders;
    }


    /**
    * @Description:  将分页处理后的数据存入PageDTO中
    * @Param: page:第几页
    * @Param: size：一页多少条数据
    * @return:
    * @Date: 2020/1/27
    */
    public PageDTO list(Integer page,Integer size)
    {
        PageDTO<OrderDTO> pageDTO=new PageDTO();
        Integer totalCount;
        totalCount = orderMapper.orderCount();
        pageDTO.setPageDTO(totalCount,page,size);

        Integer offset=size*(page-1);//偏移量
        List<Order> orders=orderMapper.list(offset,size);//分页
        List<OrderDTO> orderDTOS=ToDTOS(orders);
        pageDTO.setDataDTOS(orderDTOS);
        return pageDTO;
    }

    /**
    * @Description: 将传入的订单转换成DTO
    * @Param:  订单列表
    * @return:  转换后的DTO列表
    * @Date: 2020/1/27
    */
    private List<OrderDTO> ToDTOS(List<Order> orders) {
        List<OrderDTO> orderDTOS=new ArrayList();
        for(Order order:orders){
            OrderDTO orderDTO=new OrderDTO();
            User user=userMapper.findById(order.getUserId());
            Seat seat=seatMapper.findById(order.getSeatId());
            BeanUtils.copyProperties(order,orderDTO);//把food的所有相同属性拷贝到foodDTO上面
            orderDTO.setUserName(user.getUserName());
            orderDTO.setLocation(seat.getLocation());
            orderDTO.setOrderStatus(OrderStatusEnum.valueOf(order.getOrderStatus()).getMessage());//将订单状态转成中文
            orderDTOS.add(orderDTO);
        }
        return orderDTOS;
    }

    //删除订单
    public void deleteOrder(Long orderId){
        orderMapper.deleteOrderById(orderId);
    }

    //撤销订单
    public  void setStatusToRequestReturn(Long orderId) {

    }

    public void changeOrderStatus(Long orderId, String status) {
        orderMapper.changeOrderStatus(orderId,status);
    }
}
