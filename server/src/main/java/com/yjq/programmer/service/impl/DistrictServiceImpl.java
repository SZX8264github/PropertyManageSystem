package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.BuildingMapper;
import com.yjq.programmer.dao.DistrictMapper;
import com.yjq.programmer.dao.HouseMapper;
import com.yjq.programmer.dao.ParkingMapper;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.BuildingDTO;
import com.yjq.programmer.dto.DistrictDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.service.IBuildingService;
import com.yjq.programmer.service.IDistrictService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.CopyUtil;
import com.yjq.programmer.utils.UuidUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-02 16:02
 */
@Service
@Transactional
public class DistrictServiceImpl implements IDistrictService {

    @Resource
    private DistrictMapper districtMapper;

    @Resource
    private BuildingMapper buildingMapper;

    @Resource
    private HouseMapper houseMapper;

    @Resource
    private ParkingMapper parkingMapper;

    @Resource
    private IBuildingService buildingService;

    /**
     * 分页获取小区数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<DistrictDTO>> getDistrictList(PageDTO<DistrictDTO> pageDTO) {
        DistrictExample districtExample = new DistrictExample();
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        // 不知道每页多少条记录，默认为每页5条记录
        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        DistrictExample.Criteria c1 = districtExample.createCriteria();
        if(pageDTO.getParam() != null) {
            DistrictDTO districtDTO = pageDTO.getParam();
            if(!CommonUtil.isEmpty(districtDTO.getName())) {
                c1.andNameLike("%" + districtDTO.getName() + "%");
            }
            if(!CommonUtil.isEmpty(districtDTO.getLocation())) {
                c1.andLocationLike("%" + districtDTO.getLocation() + "%");
            }
            if(!CommonUtil.isEmpty(districtDTO.getId())) {
                c1.andIdEqualTo(districtDTO.getId());
            }
        }
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出小区数据
        List<District> districtList = districtMapper.selectByExample(districtExample);
        PageInfo<District> pageInfo = new PageInfo<>(districtList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 将domain类型数据  转成 DTO类型数据
        List<DistrictDTO> districtDTOList = CopyUtil.copyList(districtList, DistrictDTO.class);
        for(DistrictDTO districtDTO : districtDTOList) {
            // 查询此小区的楼栋数
            BuildingExample buildingExample = new BuildingExample();
            buildingExample.createCriteria().andDistrictIdEqualTo(districtDTO.getId());
            List<Building> buildingList = buildingMapper.selectByExample(buildingExample);
            int buildingTotal = (int) buildingList.stream().map(Building::getName).distinct().count();
            districtDTO.setBuildingTotal(buildingTotal);
            // 查询此小区的房屋数
            List<String> buildingIdList = buildingList.stream().map(Building::getId).collect(Collectors.toList());
            if(buildingIdList.size() > 0) {
                HouseExample houseExample = new HouseExample();
                houseExample.createCriteria().andBuildingIdIn(buildingIdList);
                int houseTotal = houseMapper.selectByExample(houseExample).size();
                districtDTO.setHouseTotal(houseTotal);
            } else {
                districtDTO.setHouseTotal(0);
            }
            // 查询此小区的车位数
            ParkingExample parkingExample = new ParkingExample();
            parkingExample.createCriteria().andDistrictIdEqualTo(districtDTO.getId());
            districtDTO.setParkingTotal(parkingMapper.selectByExample(parkingExample).size());
        }
        pageDTO.setList(districtDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存小区信息
     * @param districtDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveDistrict(DistrictDTO districtDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(districtDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        District district = CopyUtil.copy(districtDTO, District.class);
        if(CommonUtil.isEmpty(district.getId())) {
            // 添加操作
            district.setId(UuidUtil.getShortUuid());
            if(districtMapper.insertSelective(district) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.DISTRICT_ADD_ERROR);
            }
        } else {
            // 修改操作
            if(districtMapper.updateByPrimaryKeySelective(district) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.DISTRICT_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存成功！");
    }

    /**
     * 删除小区信息
     * @param districtDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteDistrict(DistrictDTO districtDTO) {
        if(CommonUtil.isEmpty(districtDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除小区信息
        if(districtMapper.deleteByPrimaryKey(districtDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.DISTRICT_DELETE_ERROR);
        }
        // 删除小区的楼栋信息
        BuildingExample buildingExample = new BuildingExample();
        buildingExample.createCriteria().andDistrictIdEqualTo(districtDTO.getId());
        List<Building> buildingList = buildingMapper.selectByExample(buildingExample);
        for(Building building : buildingList) {
            buildingService.deleteBuilding(CopyUtil.copy(building, BuildingDTO.class));
        }
        // 删除小区的车位信息
        ParkingExample parkingExample = new ParkingExample();
        parkingExample.createCriteria().andDistrictIdEqualTo(districtDTO.getId());
        parkingMapper.deleteByExample(parkingExample);
        return ResponseDTO.successByMsg(true, "删除成功！");
    }

    /**
     * 获取所有小区数据
     * @return
     */
    @Override
    public ResponseDTO<List<DistrictDTO>> getAllDistrictList() {
        DistrictExample districtExample = new DistrictExample();
        List<District> districtList = districtMapper.selectByExample(districtExample);
        List<DistrictDTO> districtDTOList = CopyUtil.copyList(districtList, DistrictDTO.class);
        return ResponseDTO.success(districtDTOList);
    }
}
