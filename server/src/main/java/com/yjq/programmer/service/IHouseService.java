package com.yjq.programmer.service;

import com.yjq.programmer.dto.HouseDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-03 16:39
 */
public interface IHouseService {

    // 分页获取房屋数据
    ResponseDTO<PageDTO<HouseDTO>> getHouseList(PageDTO<HouseDTO> pageDTO);

    // 保存房屋信息
    ResponseDTO<Boolean> saveHouse(HouseDTO houseDTO);

    // 删除房屋信息
    ResponseDTO<Boolean> deleteHouse(HouseDTO houseDTO);
}
