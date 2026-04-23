package com.yjq.programmer.controller;

import com.yjq.programmer.dto.HouseDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IHouseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-03 17:01
 */
@RestController("HouseController")
@RequestMapping("/house")
public class HouseController {

    @Resource
    private IHouseService houseService;

    /**
     * 分页获取房屋数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<HouseDTO>> getHouseList(@RequestBody PageDTO<HouseDTO> pageDTO){
        return houseService.getHouseList(pageDTO);
    }

    /**
     * 删除房屋信息
     * @param houseDTO
     * @return
     */
    @PostMapping("/delete")
    public ResponseDTO<Boolean> deleteHouse(@RequestBody HouseDTO houseDTO){
        return houseService.deleteHouse(houseDTO);
    }

    /**
     * 保存房屋信息
     * @param houseDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveHouse(@RequestBody HouseDTO houseDTO){
        return houseService.saveHouse(houseDTO);
    }
}
