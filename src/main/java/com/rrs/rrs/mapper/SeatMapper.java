package com.rrs.rrs.mapper;

import com.rrs.rrs.model.Seat;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface SeatMapper {
    @Select("select * from seat_table where seat_id=#{seatId}")
    Seat findById(@Param("seatId") Integer seatId);

    @Update("update seat_table set seat_status=#{seatStatus}, where seat_id=#{seatId}")
    void changeSeatStatus(@Param("seatId")Integer seatId,@Param("seatStatus") String seatStatus);

    @Insert("insert into seat_table(location,seat_status)values(#{location},#{seat_status})")
    void createSeat(Seat seat);

    @Select("select * from seat_table")
    List<Seat> selectAll();

    @Select("select count(1) from seat_table")
    Integer seatCountAll();

    @Select("select * from seat_table  order by seat_id limit #{offset},#{size}")
    List<Seat> list(@Param(value = "offset")Integer offset, @Param(value = "size")Integer size);
}
