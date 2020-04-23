package com.rrs.rrs.mapper;

import com.rrs.rrs.model.Seat;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface SeatMapper {
    //根据id查座位
    @Select("select * from seat_table where seat_id=#{seatId}")
    Seat findById(@Param("seatId") Integer seatId);

    //改变座位状态
    @Update("update seat_table set seat_status=#{seatStatus} where seat_id=#{seatId}")
    void changeSeatStatus(@Param("seatId")Integer seatId,@Param("seatStatus") String seatStatus);

    //创建座位
    @Insert("insert into seat_table(location,seat_status)values(#{location},#{seatStatus})")
    void createSeat(Seat seat);

    //查询所有座位
    @Select("select * from seat_table")
    List<Seat> selectAll();

    //查询所有座位的数量
    @Select("select count(1) from seat_table")
    Integer seatCountAll();

    //查询所有座位并进行分页处理
    @Select("select * from seat_table  order by seat_id limit #{offset},#{size}")
    List<Seat> list(@Param(value = "offset")Integer offset, @Param(value = "size")Integer size);

    //根据id删除座位
    @Delete("delete from seat_table where seat_id=#{seatId}")
    void deleteSeat(@Param(value = "seatId")Integer seatId);

    //根据位置查找座位
    @Select("select * from seat_table where location=#{location}")
    Seat findByLocation(@Param(value = "location")String location);

    //根据状态查找座位并进行分页处理
    @Select("select * from seat_table where seat_status=#{status} order by seat_id limit #{offset},#{size}")
    List<Seat> listByStatus(@Param(value = "offset")Integer offset, @Param(value = "size")Integer size,@Param(value = "status")String status);

    //查找某状态座位的数量
    @Select("select count(1) from seat_table where seat_status=#{status}")
    Integer seatCountByStatus(@Param(value = "status")String status);
}
