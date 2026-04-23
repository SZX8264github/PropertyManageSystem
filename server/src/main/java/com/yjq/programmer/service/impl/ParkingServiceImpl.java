package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.DistrictMapper;
import com.yjq.programmer.dao.ParkingMapper;
import com.yjq.programmer.dao.UserMapper;
import com.yjq.programmer.domain.District;
import com.yjq.programmer.domain.Parking;
import com.yjq.programmer.domain.ParkingExample;
import com.yjq.programmer.domain.User;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.service.IParkingService;
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
 * @create 2024-06-04 10:54
 */
@Service
@Transactional
public class ParkingServiceImpl implements IParkingService {

    @Resource
    private ParkingMapper parkingMapper;

    @Resource
    private DistrictMapper districtMapper;

    @Resource
    private UserMapper userMapper;


    /**
     * 分页获取车位数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<ParkingDTO>> getParkingList(PageDTO<ParkingDTO> pageDTO) {
        ParkingExample parkingExample = new ParkingExample();
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        // 不知道每页多少条记录，默认为每页5条记录
        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        ParkingExample.Criteria c1 = parkingExample.createCriteria();
        if(pageDTO.getParam() != null) {
            ParkingDTO parkingDTO = pageDTO.getParam();
            if(!CommonUtil.isEmpty(parkingDTO.getName())) {
                c1.andNameLike("%" + parkingDTO.getName() + "%");
            }
            if(!CommonUtil.isEmpty(parkingDTO.getDistrictId())) {
                c1.andDistrictIdEqualTo(parkingDTO.getDistrictId());
            }
        }
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出车位数据
        List<Parking> parkingList = parkingMapper.selectByExample(parkingExample);
        PageInfo<Parking> pageInfo = new PageInfo<>(parkingList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 将domain类型数据  转成 DTO类型数据
        List<ParkingDTO> parkingDTOList = CopyUtil.copyList(parkingList, ParkingDTO.class);
        for(ParkingDTO parkingDTO : parkingDTOList) {
            District district = districtMapper.selectByPrimaryKey(parkingDTO.getDistrictId());
            parkingDTO.setDistrictDTO(CopyUtil.copy(district, DistrictDTO.class));
            User user = userMapper.selectByPrimaryKey(parkingDTO.getUserId());
            parkingDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));
        }
        pageDTO.setList(parkingDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存车位信息
     * @param parkingDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveParking(ParkingDTO parkingDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(parkingDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        Parking parking = CopyUtil.copy(parkingDTO, Parking.class);
        if(CommonUtil.isEmpty(parking.getId())) {
            // 添加操作
            parking.setId(UuidUtil.getShortUuid());
            if(parkingMapper.insertSelective(parking) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.PARKING_ADD_ERROR);
            }
        } else {
            // 修改操作
            if(parkingMapper.updateByPrimaryKeySelective(parking) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.PARKING_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存成功！");
    }

    /**
     * 删除车位信息
     * @param parkingDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteParking(ParkingDTO parkingDTO) {
        if(CommonUtil.isEmpty(parkingDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除车位信息
        if(parkingMapper.deleteByPrimaryKey(parkingDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.PARKING_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "删除成功！");
    }
}
