package com.rrs.rrs.service;


import com.rrs.rrs.dto.DataQueryDTO;
import com.rrs.rrs.dto.OrderDTO;
import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.dto.QueryDTO;
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

import java.util.*;

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
            BeanUtils.copyProperties(order,orderDTO);//把order的所有相同属性拷贝到orderDTO上面
            if (user==null){
                orderDTO.setUserName("被注销账号的用户");
            }else {
                orderDTO.setUserName(user.getUserName());
            }
            if (seat==null){
                orderDTO.setLocation("该位置已更新");
            }else {
                orderDTO.setLocation(seat.getLocation());
            }
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

    //预定成功
    public void orderApplyOK(Long orderId) {
        Order order=orderMapper.findById(orderId);
        changeOrderStatus(orderId, OrderStatusEnum.APPLY_OK.getStatus());//将订单状态变为预订成功
        seatMapper.changeSeatStatus(order.getSeatId(),"有人");//将预定的位置变为有人

    }

    //订单完成
    public void orderFinish(Long orderId) {
        Order order=orderMapper.findById(orderId);
        changeOrderStatus(orderId,OrderStatusEnum.FINISH.getStatus());//将订单状态变为已完成
        seatMapper.changeSeatStatus(order.getSeatId(),"空");//将预定的位置变为空

    }

    private void changeOrderStatus(Long orderId, String status) {
        orderMapper.changeOrderStatus(orderId,status);
    }

    //获取排名前l的下单次数最多的顾客
    public List getUserRankData(int l) {

        //获取所有的订单状态为已完成的订单信息
        ArrayList<Order> analysisList=orderMapper.getAllOrderStatusTrue();

        //统计各个用户下单的次数
        HashMap<Long,Integer> analysisMap=new HashMap();
        for (Order order:analysisList) {
            Long userId=order.getUserId();
            if (analysisMap.containsKey(userId)){//如果analysisMap中有该用户的id，计数加1
                analysisMap.replace(userId,analysisMap.get(userId)+1);
            }else {//果analysisMap中没有该用户的id，以该id为键1为值加入analysisMap中
                analysisMap.put(userId,1);
            }
        }

        //对统计数据进行排序
        List<HashMap.Entry<Long,Integer> > sortList = new ArrayList<HashMap.Entry<Long,Integer> >(analysisMap.entrySet());
        Collections.sort(sortList, new Comparator<HashMap.Entry<Long,Integer> >() {

            //降序排序
            @Override
            public int compare(HashMap.Entry<Long,Integer> o1, HashMap.Entry<Long,Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        //获取排名前l条数据
        if (sortList.size() > l) {//判断list长度
            sortList = sortList.subList(0, l);//取前l条数据
        }

        //将userId和数量分离
        List userList=new ArrayList();
        List qtyList=new ArrayList();
        for (HashMap.Entry<Long,Integer> item:sortList) {
            User user=userMapper.findById(item.getKey());
            userList.add(user.getUserName());
            qtyList.add(item.getValue());
        }
        List resultList=new ArrayList();
        resultList.add(userList);
        resultList.add(qtyList);


        return resultList;
    }

    //查询订单
    public PageDTO listSearch(int page, int size, String name, String status, String condition) {
        PageDTO<OrderDTO> pageDTO=new PageDTO();
        Integer offset=size*(page-1);//偏移量
        //构建查询条件
        DataQueryDTO dataQueryDTO=new DataQueryDTO();
        dataQueryDTO.setOffset(offset);
        dataQueryDTO.setSize(size);

        //对订单状态进行转换
        for (OrderStatusEnum orderStatus:OrderStatusEnum.values()) {
            if (status.equals(orderStatus.getMessage())){
                status=orderStatus.getStatus();
                break;
            }
        }
        if (status.equals("全部"))status="^";

        dataQueryDTO.setStatus(status);

        List<Order> orderSum=new ArrayList();

        if (name.equals("全部")){
            List<Order> orders=new ArrayList<>();
            if (status.equals("^")){//获取全部订单
                orders=orderMapper.getAllOrder();
            }
            else {//根据状态获取订单信息
                orders=orderMapper.getOrdersByStatus(status);
            }
            orderSum.addAll(orders);
        }
        else  if (condition.equals("预订人")){//根据预订人查订单


                List<User> users=userMapper.listSearchByName(name);
                for (User user:users) {
                    dataQueryDTO.setId(user.getUserId());
                    List<Order> orders=orderMapper.listSearchByUserId(dataQueryDTO);//查询某位联系人的订单信息
                    orderSum.addAll(orders);
                }


        }else {//根据订单编号查订单
            try {
                Order order=orderMapper.findById(Long.parseLong(name));
                orderSum.add(order);
            }catch (Exception e){
            }

        }

        List<OrderDTO> orderDTOS=ToDTOS(orderSum);//将订单转换成DTO
        pageDTO.setPageDTO(orderDTOS.size(),page,size);
        pageDTO.setDataDTOS(orderDTOS);
        return pageDTO;


    }
}
