package com.yjq.programmer.dao;

import com.yjq.programmer.domain.Parking;
import com.yjq.programmer.domain.ParkingExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ParkingMapper {
    int countByExample(ParkingExample example);

    int deleteByExample(ParkingExample example);

    int deleteByPrimaryKey(String id);

    int insert(Parking record);

    int insertSelective(Parking record);

    List<Parking> selectByExample(ParkingExample example);

    Parking selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Parking record, @Param("example") ParkingExample example);

    int updateByExample(@Param("record") Parking record, @Param("example") ParkingExample example);

    int updateByPrimaryKeySelective(Parking record);

    int updateByPrimaryKey(Parking record);
}
