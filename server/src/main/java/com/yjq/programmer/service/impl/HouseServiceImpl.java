package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.BuildingMapper;
import com.yjq.programmer.dao.HouseMapper;
import com.yjq.programmer.dao.UserMapper;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.service.IHouseService;
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
 * @create 2024-06-03 16:39
 */
@Service
@Transactional
public class HouseServiceImpl implements IHouseService {

    @Resource
    private HouseMapper houseMapper;

    @Resource
    private BuildingMapper buildingMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 分页获取房屋数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<HouseDTO>> getHouseList(PageDTO<HouseDTO> pageDTO) {
        HouseExample houseExample = new HouseExample();
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        // 不知道每页多少条记录，默认为每页5条记录
        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        HouseExample.Criteria c1 = houseExample.createCriteria();
        if(pageDTO.getParam() != null) {
            HouseDTO houseDTO = pageDTO.getParam();
            if(!CommonUtil.isEmpty(houseDTO.getCard())) {
                c1.andCardLike("%" + houseDTO.getCard() + "%");
            }
            if(!CommonUtil.isEmpty(houseDTO.getBuildingId())) {
                c1.andBuildingIdEqualTo(houseDTO.getBuildingId());
            }
            if(!CommonUtil.isEmpty(houseDTO.getDistrictId())) {
                BuildingExample buildingExample = new BuildingExample();
                buildingExample.createCriteria().andDistrictIdEqualTo(houseDTO.getDistrictId());
                List<String> buildingIdList = buildingMapper.selectByExample(buildingExample).stream().map(Building::getId).collect(Collectors.toList());
                if(buildingIdList.size() > 0) {
                    c1.andBuildingIdIn(buildingIdList);
                } else {
                    c1.andBuildingIdEqualTo("-");
                }
            }
            if(!CommonUtil.isEmpty(houseDTO.getUserId())) {
                c1.andUserIdEqualTo(houseDTO.getUserId());
            }
        }
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出房屋数据
        List<House> houseList = houseMapper.selectByExample(houseExample);
        PageInfo<House> pageInfo = new PageInfo<>(houseList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 将domain类型数据  转成 DTO类型数据
        List<HouseDTO> houseDTOList = CopyUtil.copyList(houseList, HouseDTO.class);
        for(HouseDTO houseDTO : houseDTOList) {
            Building building = buildingMapper.selectByPrimaryKey(houseDTO.getBuildingId());
            houseDTO.setBuildingDTO(CopyUtil.copy(building, BuildingDTO.class));
            User user = userMapper.selectByPrimaryKey(houseDTO.getUserId());
            houseDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));
        }
        pageDTO.setList(houseDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存房屋信息
     * @param houseDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveHouse(HouseDTO houseDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(houseDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        House house = CopyUtil.copy(houseDTO, House.class);
        if(CommonUtil.isEmpty(house.getId())) {
            // 添加操作
            // 判断房屋是否存在
            if(isHouseExist(house, "")){
                return ResponseDTO.errorByMsg(CodeMsg.HOUSE_EXIST);
            }
            house.setId(UuidUtil.getShortUuid());
            if(houseMapper.insertSelective(house) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.HOUSE_ADD_ERROR);
            }
        } else {
            // 修改操作
            // 判断房屋是否存在
            if(isHouseExist(house, house.getId())){
                return ResponseDTO.errorByMsg(CodeMsg.HOUSE_EXIST);
            }
            if(houseMapper.updateByPrimaryKeySelective(house) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.HOUSE_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存成功！");
    }

    /**
     * 删除房屋信息
     * @param houseDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteHouse(HouseDTO houseDTO) {
        if(CommonUtil.isEmpty(houseDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除房屋信息
        if(houseMapper.deleteByPrimaryKey(houseDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.HOUSE_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "删除成功！");
    }


    /**
     * 判断房屋信息是否存在
     * @param house
     * @param id
     * @return
     */
    public Boolean isHouseExist(House house, String id) {
        HouseExample houseExample = new HouseExample();
        houseExample.createCriteria().andBuildingIdEqualTo(house.getBuildingId()).andCardEqualTo(house.getCard());
        List<House> selectedHouseList = houseMapper.selectByExample(houseExample);
        if (selectedHouseList != null && selectedHouseList.size() > 0) {
            if (selectedHouseList.size() > 1) {
                return true; //出现重复
            }
            if (!selectedHouseList.get(0).getId().equals(id)) {
                return true; //出现重复
            }
        }
        return false;//没有重复
    }
}
