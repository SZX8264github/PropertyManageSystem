package com.yjq.programmer.service;

import com.yjq.programmer.dto.BuildingDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;

import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-03 13:38
 */
public interface IBuildingService {

    // 分页获取楼栋数据
    ResponseDTO<PageDTO<BuildingDTO>> getBuildingList(PageDTO<BuildingDTO> pageDTO);

    // 保存楼栋信息
    ResponseDTO<Boolean> saveBuilding(BuildingDTO buildingDTO);

    // 删除楼栋信息
    ResponseDTO<Boolean> deleteBuilding(BuildingDTO buildingDTO);

    // 获取所有楼栋数据
    ResponseDTO<List<BuildingDTO>> getAllBuildingList(UserDTO userDTO);
}
