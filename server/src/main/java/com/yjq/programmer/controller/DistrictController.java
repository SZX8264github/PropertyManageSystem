package com.yjq.programmer.controller;

import com.yjq.programmer.dto.DistrictDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IDistrictService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-02 16:07
 */
@RestController("DistrictController")
@RequestMapping("/district")
public class DistrictController {

    @Resource
    private IDistrictService districtService;

    /**
     * 分页获取小区数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<DistrictDTO>> getDistrictList(@RequestBody PageDTO<DistrictDTO> pageDTO){
        return districtService.getDistrictList(pageDTO);
    }

    /**
     * 删除小区信息
     * @param districtDTO
     * @return
     */
    @PostMapping("/delete")
    public ResponseDTO<Boolean> deleteDistrict(@RequestBody DistrictDTO districtDTO){
        return districtService.deleteDistrict(districtDTO);
    }

    /**
     * 保存小区信息
     * @param districtDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveDistrict(@RequestBody DistrictDTO districtDTO){
        return districtService.saveDistrict(districtDTO);
    }

    /**
     * 获取所有小区数据
     * @return
     */
    @PostMapping("/all")
    public ResponseDTO<List<DistrictDTO>> getAllDistrictList(@RequestBody(required = false) Object obj){
        return districtService.getAllDistrictList();
    }


}
