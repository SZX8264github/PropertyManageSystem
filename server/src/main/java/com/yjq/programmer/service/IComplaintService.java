package com.yjq.programmer.service;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ComplaintDTO;
import com.yjq.programmer.dto.ResponseDTO;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-10 9:24
 */
public interface IComplaintService {

    // 分页获取投诉数据
    ResponseDTO<PageDTO<ComplaintDTO>> getComplaintList(PageDTO<ComplaintDTO> pageDTO);

    // 保存投诉信息
    ResponseDTO<Boolean> saveComplaint(ComplaintDTO complaintDTO);

    // 删除投诉信息
    ResponseDTO<Boolean> deleteComplaint(ComplaintDTO complaintDTO);
}
