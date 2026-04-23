package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.BuildingMapper;
import com.yjq.programmer.dao.DistrictMapper;
import com.yjq.programmer.dao.HouseMapper;
import com.yjq.programmer.domain.Building;
import com.yjq.programmer.domain.BuildingExample;
import com.yjq.programmer.domain.District;
import com.yjq.programmer.domain.HouseExample;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.service.IBuildingService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.CopyUtil;
import com.yjq.programmer.utils.UuidUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-03 13:38
 */
@Service
@Transactional
public class BuildingServiceImpl implements IBuildingService {

    @Resource
    private BuildingMapper buildingMapper;

    @Resource
    private DistrictMapper districtMapper;

    @Resource
    private HouseMapper houseMapper;

    /**
     * 分页获取楼栋数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<BuildingDTO>> getBuildingList(PageDTO<BuildingDTO> pageDTO) {
        BuildingExample buildingExample = new BuildingExample();
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        // 不知道每页多少条记录，默认为每页5条记录
        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        BuildingExample.Criteria c1 = buildingExample.createCriteria();
        if(pageDTO.getParam() != null) {
            BuildingDTO buildingDTO = pageDTO.getParam();
            if(!CommonUtil.isEmpty(buildingDTO.getName())) {
                c1.andNameLike("%" + buildingDTO.getName() + "%");
            }
            if(!CommonUtil.isEmpty(buildingDTO.getDistrictId())) {
                c1.andDistrictIdEqualTo(buildingDTO.getDistrictId());
            }
        }
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出楼栋数据
        List<Building> buildingList = buildingMapper.selectByExample(buildingExample);
        PageInfo<Building> pageInfo = new PageInfo<>(buildingList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 将domain类型数据  转成 DTO类型数据
        List<BuildingDTO> buildingDTOList = CopyUtil.copyList(buildingList, BuildingDTO.class);
        for(BuildingDTO buildingDTO : buildingDTOList) {
            District district = districtMapper.selectByPrimaryKey(buildingDTO.getDistrictId());
            buildingDTO.setDistrictDTO(CopyUtil.copy(district, DistrictDTO.class));
        }
        pageDTO.setList(buildingDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存楼栋信息
     * @param buildingDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveBuilding(BuildingDTO buildingDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(buildingDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        Building building = CopyUtil.copy(buildingDTO, Building.class);
        if(CommonUtil.isEmpty(building.getId())) {
            // 添加操作
            // 判断楼栋是否存在
            if(isBuildingExist(building, "")){
                return ResponseDTO.errorByMsg(CodeMsg.BUILDING_EXIST);
            }
            building.setId(UuidUtil.getShortUuid());
            if(buildingMapper.insertSelective(building) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.BUILDING_ADD_ERROR);
            }
        } else {
            // 修改操作
            // 判断楼栋是否存在
            if(isBuildingExist(building, building.getId())){
                return ResponseDTO.errorByMsg(CodeMsg.BUILDING_EXIST);
            }
            if(buildingMapper.updateByPrimaryKeySelective(building) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.BUILDING_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存成功！");
    }

    /**
     * 删除楼栋信息
     * @param buildingDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteBuilding(BuildingDTO buildingDTO) {
        if(CommonUtil.isEmpty(buildingDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除楼栋信息
        if(buildingMapper.deleteByPrimaryKey(buildingDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.BUILDING_DELETE_ERROR);
        }
        // 删除有关房屋信息
        HouseExample houseExample = new HouseExample();
        houseExample.createCriteria().andBuildingIdEqualTo(buildingDTO.getId());
        houseMapper.deleteByExample(houseExample);
        return ResponseDTO.successByMsg(true, "删除成功！");
    }


    /**
     * 获取所有楼栋数据
     * @return
     */
    @Override
    public ResponseDTO<List<BuildingDTO>> getAllBuildingList(UserDTO userDTO) {
        BuildingExample buildingExample = new BuildingExample();
        BuildingExample.Criteria criteria = buildingExample.createCriteria();
        if(!CommonUtil.isEmpty(userDTO.getDistrictId())) {
            criteria.andDistrictIdEqualTo(userDTO.getDistrictId());
        }
        List<Building> buildingList = buildingMapper.selectByExample(buildingExample);
        List<BuildingDTO> buildingDTOList = CopyUtil.copyList(buildingList, BuildingDTO.class);
        return ResponseDTO.success(buildingDTOList);
    }

    /**
     * 判断楼栋信息是否存在
     * @param building
     * @param id
     * @return
     */
    public Boolean isBuildingExist(Building building, String id) {
        BuildingExample buildingExample = new BuildingExample();
        buildingExample.createCriteria().andNameEqualTo(building.getName()).andUnitNameEqualTo(building.getUnitName()).andDistrictIdEqualTo(building.getDistrictId());
        List<Building> selectedBuildingList = buildingMapper.selectByExample(buildingExample);
        if (selectedBuildingList != null && selectedBuildingList.size() > 0) {
            if (selectedBuildingList.size() > 1) {
                return true; //出现重复
            }
            if (!selectedBuildingList.get(0).getId().equals(id)) {
                return true; //出现重复
            }
        }
        return false;//没有重复
    }
}
