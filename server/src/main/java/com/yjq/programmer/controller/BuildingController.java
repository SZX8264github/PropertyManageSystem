package com.yjq.programmer.controller;

import com.yjq.programmer.dto.BuildingDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.service.IBuildingService;
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
 * @create 2024-06-03 13:41
 */
@RestController("BuildingController")
@RequestMapping("/building")
public class BuildingController {

    @Resource
    private IBuildingService buildingService;

    /**
     * 分页获取楼栋数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<BuildingDTO>> getBuildingList(@RequestBody PageDTO<BuildingDTO> pageDTO){
        return buildingService.getBuildingList(pageDTO);
    }

    /**
     * 删除楼栋信息
     * @param buildingDTO
     * @return
     */
    @PostMapping("/delete")
    public ResponseDTO<Boolean> deleteBuilding(@RequestBody BuildingDTO buildingDTO){
        return buildingService.deleteBuilding(buildingDTO);
    }

    /**
     * 保存楼栋信息
     * @param buildingDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveBuilding(@RequestBody BuildingDTO buildingDTO){
        return buildingService.saveBuilding(buildingDTO);
    }

    /**
     * 获取所有楼栋数据
     * @return
     */
    @PostMapping("/all")
    public ResponseDTO<List<BuildingDTO>> getAllBuildingList(@RequestBody UserDTO userDTO){
        return buildingService.getAllBuildingList(userDTO);
    }

}
