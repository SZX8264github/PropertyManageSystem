package com.yjq.programmer.controller;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ComplaintDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IComplaintService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-10 9:34
 */
@RestController("ComplaintController")
@RequestMapping("/complaint")
public class ComplaintController {

    @Resource
    private IComplaintService complaintService;

    /**
     * 分页获取投诉数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<ComplaintDTO>> getComplaintList(@RequestBody PageDTO<ComplaintDTO> pageDTO){
        return complaintService.getComplaintList(pageDTO);
    }

    /**
     * 删除投诉信息
     * @param complaintDTO
     * @return
     */
    @PostMapping("/delete")
    public ResponseDTO<Boolean> deleteComplaint(@RequestBody ComplaintDTO complaintDTO){
        return complaintService.deleteComplaint(complaintDTO);
    }

    /**
     * 保存投诉信息
     * @param complaintDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveComplaint(@RequestBody ComplaintDTO complaintDTO){
        return complaintService.saveComplaint(complaintDTO);
    }
}
