package com.yjq.programmer.controller;

import com.yjq.programmer.dto.ParkingDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IParkingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-04 10:59
 */
@RestController("ParkingController")
@RequestMapping("/parking")
public class ParkingController {

    @Resource
    private IParkingService parkingService;

    /**
     * 分页获取车位数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<ParkingDTO>> getParkingList(@RequestBody PageDTO<ParkingDTO> pageDTO){
        return parkingService.getParkingList(pageDTO);
    }

    /**
     * 删除车位信息
     * @param parkingDTO
     * @return
     */
    @PostMapping("/delete")
    public ResponseDTO<Boolean> deleteParking(@RequestBody ParkingDTO parkingDTO){
        return parkingService.deleteParking(parkingDTO);
    }

    /**
     * 保存车位信息
     * @param parkingDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveParking(@RequestBody ParkingDTO parkingDTO){
        return parkingService.saveParking(parkingDTO);
    }
}
