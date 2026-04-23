package com.yjq.programmer.service;

import com.yjq.programmer.dto.DistrictDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;

import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-02 16:02
 */
public interface IDistrictService {

    // 分页获取小区数据
    ResponseDTO<PageDTO<DistrictDTO>> getDistrictList(PageDTO<DistrictDTO> pageDTO);

    // 保存小区信息
    ResponseDTO<Boolean> saveDistrict(DistrictDTO districtDTO);

    // 删除小区信息
    ResponseDTO<Boolean> deleteDistrict(DistrictDTO districtDTO);

    // 获取所有小区数据
    ResponseDTO<List<DistrictDTO>> getAllDistrictList();
}
