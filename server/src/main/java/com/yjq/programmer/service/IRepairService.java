package com.yjq.programmer.service;

import com.yjq.programmer.dto.RepairDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-09 20:30
 */
public interface IRepairService {

    // 分页获取维修数据
    ResponseDTO<PageDTO<RepairDTO>> getRepairList(PageDTO<RepairDTO> pageDTO);

    // 保存维修信息
    ResponseDTO<Boolean> saveRepair(RepairDTO repairDTO);

    // 删除维修信息
    ResponseDTO<Boolean> deleteRepair(RepairDTO repairDTO);

}
