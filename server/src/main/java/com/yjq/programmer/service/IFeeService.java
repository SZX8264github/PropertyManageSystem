package com.yjq.programmer.service;

import com.yjq.programmer.dto.FeeDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;

import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-05 14:43
 */
public interface IFeeService {


    // 分页获取缴费数据
    ResponseDTO<PageDTO<FeeDTO>> getFeeList(PageDTO<FeeDTO> pageDTO);

    // 保存缴费信息
    ResponseDTO<Boolean> saveFee(FeeDTO feeDTO);

    // 删除缴费信息
    ResponseDTO<Boolean> deleteFee(FeeDTO feeDTO);

    // 获取近五个月缴费数据
    ResponseDTO<Map<String, Object>> getFeeStatistic(UserDTO userDTO);
}
