package com.rrs.rrs.service;

import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.mapper.OrderMapper;
import com.rrs.rrs.mapper.SeatMapper;
import com.rrs.rrs.model.Order;
import com.rrs.rrs.model.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {
    @Autowired
    private SeatMapper seatMapper;
    @Autowired
    private OrderMapper orderMapper;

    //寻找餐台
    public Seat findSeatById(Integer id){
        return seatMapper.findById(id);
    }
    public Seat findSeatByLocation(String location){
        return seatMapper.findByLocation(location);
    }

    public PageDTO findSeatByLocation(String location,int page,int size){
        PageDTO<Seat> pageDTO=new PageDTO();
        //根据具体位置查餐台
        Seat seat=seatMapper.findByLocation(location);
        List<Seat> seats = new ArrayList();
        seats.add(seat);
        pageDTO.setDataDTOS(seats);
        pageDTO.setPageDTO(seats.size(),page,size);
        return pageDTO;
    }


    //改变餐台状态
    public void changeSeatStatus(Integer seatId,String seatStatus){
        seatMapper.changeSeatStatus(seatId,seatStatus);
    }

    //添加新餐台
    public void createSeat(String location){
        Seat seat=new Seat();
        seat.setLocation(location);
//        seat.setSeatStatus("空");
        seatMapper.createSeat(seat);
    }



    //查询所有餐台并进行分页处理
    public PageDTO list(Integer page, Integer size) {

        PageDTO<Seat> pageDTO=new PageDTO();
        Integer totalCount;
        totalCount = seatMapper.seatCountAll();
        pageDTO.setPageDTO(totalCount,page,size);

        Integer offset=size*(page-1);//偏移量
        List<Seat> seats=seatMapper.list(offset,size);//分页
        pageDTO.setDataDTOS(seats);
        return pageDTO;
    }


    //删除餐台
    public void deleteSeat(Integer seatId) {
        seatMapper.deleteSeat(seatId);
    }

    //根据状态查找餐台并进行分页
    public PageDTO listSearchStatus(int page, int size, String status) {
        PageDTO<Seat> pageDTO=new PageDTO();
        Integer totalCount;
        totalCount=seatMapper.seatCountByStatus(status);
        Integer offset=size*(page-1);//偏移量
        List<Seat> seats=seatMapper.listByStatus(offset,size,status);//分页
        pageDTO.setPageDTO(totalCount,page,size);
        pageDTO.setDataDTOS(seats);
        return pageDTO;
    }

    //根据时间查询餐台
    public PageDTO searchSeatByTime(int page, int size, String orderTime) {
        PageDTO<Seat> pageDTO=new PageDTO();
        List<Order> orders1= orderMapper.getOrdersByStatus("APPLYING");//申请中的订单
        List<Order> orders2= orderMapper.getOrdersByStatus("APPLY_OK");//已接受的订单
        //申请中和已接受的订单
        List<Order> orders=new ArrayList();
        orders.addAll(orders1);
        orders.addAll(orders2);

        Integer offset=size*(page-1);//偏移量
        //获取所有的座位
        List<Seat> seats=seatMapper.selectAll();

        //如果订单中这一时间的某一座位已被预订则从座位列表中删除
        for (Order order:orders) {
            if (order.getOrderTime().equals(orderTime)){//时间相同
                seats.remove(seatMapper.findById(order.getSeatId()));//删除座位
            }
        }
        pageDTO.setPageDTO(seats.size(),page,size);

        //截取当前页的座位信息(分页)
        List<Seat> pageSeat=new ArrayList();
        for(int i = (page-1)*size; i < page*size&&i<seats.size(); i++){
            pageSeat.add(seats.get(i));
        }

        pageDTO.setDataDTOS(pageSeat);
        return pageDTO;
    }

}
