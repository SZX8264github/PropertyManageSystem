package com.yjq.programmer.controller;

import com.yjq.programmer.dto.FeeDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.service.IFeeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-05 15:09
 */
@RestController("FeeController")
@RequestMapping("/fee")
public class FeeController {

    @Resource
    private IFeeService feeService;

    /**
     * 分页获取缴费数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<FeeDTO>> getFeeList(@RequestBody PageDTO<FeeDTO> pageDTO){
        return feeService.getFeeList(pageDTO);
    }

    /**
     * 删除缴费信息
     * @param feeDTO
     * @return
     */
    @PostMapping("/delete")
    public ResponseDTO<Boolean> deleteFee(@RequestBody FeeDTO feeDTO){
        return feeService.deleteFee(feeDTO);
    }

    /**
     * 保存缴费信息
     * @param feeDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveFee(@RequestBody FeeDTO feeDTO){
        return feeService.saveFee(feeDTO);
    }


    /**
     * 获取近五个月缴费数据
     * @param userDTO
     * @return
     */
    @PostMapping("/statistic")
    public ResponseDTO<Map<String, Object>> getFeeStatistic(@RequestBody UserDTO userDTO) {
        return feeService.getFeeStatistic(userDTO);
    }
}
