package com.rrs.rrs.service;

import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.mapper.SeatMapper;
import com.rrs.rrs.model.Seat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SeatService {
    @Autowired
    private SeatMapper seatMapper;

    //寻找座位
    public Seat findSeatById(Integer id){
        return seatMapper.findById(id);
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





}
