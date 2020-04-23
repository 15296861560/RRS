package com.rrs.rrs.service;

import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.mapper.SeatMapper;
import com.rrs.rrs.model.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {
    @Autowired
    private SeatMapper seatMapper;

    //寻找座位
    public Seat findSeatById(Integer id){
        return seatMapper.findById(id);
    }
    public Seat findSeatByLocation(String location){
        return seatMapper.findByLocation(location);
    }

    public PageDTO findSeatByLocation(String location,int page,int size){
        PageDTO<Seat> pageDTO=new PageDTO();
        //根据具体位置查座位
        Seat seat=seatMapper.findByLocation(location);
        List<Seat> seats = new ArrayList();
        seats.add(seat);
        pageDTO.setDataDTOS(seats);
        pageDTO.setPageDTO(seats.size(),page,size);
        return pageDTO;
    }


    //改变座位状态
    public void changeSeatStatus(Integer seatId,String seatStatus){
        seatMapper.changeSeatStatus(seatId,seatStatus);
    }

    //添加新座位
    public void createSeat(String location){
        Seat seat=new Seat();
        seat.setLocation(location);
        seat.setSeatStatus("空");
        seatMapper.createSeat(seat);
    }



    //查询所有座位并进行分页处理
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


    //删除座位
    public void deleteSeat(Integer seatId) {
        seatMapper.deleteSeat(seatId);
    }

    //根据状态查找座位并进行分页
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
}
