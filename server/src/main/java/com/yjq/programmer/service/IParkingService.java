package com.yjq.programmer.service;

import com.yjq.programmer.dto.ParkingDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-04 10:54
 */
public interface IParkingService {

    // 分页获取车位数据
    ResponseDTO<PageDTO<ParkingDTO>> getParkingList(PageDTO<ParkingDTO> pageDTO);

    // 保存车位信息
    ResponseDTO<Boolean> saveParking(ParkingDTO parkingDTO);

    // 删除车位信息
    ResponseDTO<Boolean> deleteParking(ParkingDTO parkingDTO);
}
