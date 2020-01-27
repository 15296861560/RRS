package com.rrs.rrs.mapper;

import com.rrs.rrs.model.Seat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface SeatMapper {
    @Select("select * from seat_table where seat_id=#{seatId}")
    Seat findById(@Param("seatId") Integer seatId);
}
