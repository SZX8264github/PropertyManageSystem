package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.*;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.service.IRepairService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.CopyUtil;
import com.yjq.programmer.utils.UuidUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-09 20:31
 */
@Service
@Transactional
public class RepairServiceImpl implements IRepairService {

    @Resource
    private RepairMapper repairMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private DistrictMapper districtMapper;

    @Resource
    private BuildingMapper buildingMapper;

    @Resource
    private HouseMapper houseMapper;


    /**
     * 分页获取维修数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<RepairDTO>> getRepairList(PageDTO<RepairDTO> pageDTO) {
        RepairExample repairExample = new RepairExample();
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        // 不知道每页多少条记录，默认为每页5条记录
        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        RepairExample.Criteria c1 = repairExample.createCriteria();
        if(pageDTO.getParam() != null) {
            RepairDTO repairDTO = pageDTO.getParam();
            if(!CommonUtil.isEmpty(repairDTO.getContent())) {
                c1.andContentLike("%" + repairDTO.getContent() + "%");
            }
            if(!CommonUtil.isEmpty(repairDTO.getUserId())) {
                c1.andUserIdEqualTo(repairDTO.getUserId());
            }
            if(!CommonUtil.isEmpty(repairDTO.getLoginUserId())) {
                User loginUser = userMapper.selectByPrimaryKey(repairDTO.getLoginUserId());
                if(RoleEnum.OWNER.getCode().equals(loginUser.getRoleId())) {
                    // 业主只能看到自己的维修信息
                    c1.andUserIdEqualTo(loginUser.getId());
                } else if (RoleEnum.STAFF.getCode().equals(loginUser.getRoleId())) {
                    // 物业员工只能看到自己小区的维修信息
                    UserExample userExample = new UserExample();
                    userExample.createCriteria().andDistrictIdEqualTo(loginUser.getDistrictId());
                    List<String> userIdList = userMapper.selectByExample(userExample).stream().map(User::getId).collect(Collectors.toList());
                    c1.andUserIdIn(userIdList);
                }
            }
        }
        repairExample.setOrderByClause("create_time desc");
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出维修数据
        List<Repair> repairList = repairMapper.selectByExample(repairExample);
        PageInfo<Repair> pageInfo = new PageInfo<>(repairList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 将domain类型数据  转成 DTO类型数据
        List<RepairDTO> repairDTOList = CopyUtil.copyList(repairList, RepairDTO.class);
        for(RepairDTO repairDTO : repairDTOList) {
            User user = userMapper.selectByPrimaryKey(repairDTO.getUserId());
            // 住址
            String location = "";
            if(user != null) {
                District district = districtMapper.selectByPrimaryKey(user.getDistrictId());
                HouseExample houseExample = new HouseExample();
                houseExample.createCriteria().andUserIdEqualTo(user.getId());
                List<House> houseList = houseMapper.selectByExample(houseExample);
                location += Optional.ofNullable(district.getName()).orElse("");
                if(houseList.size() > 0) {
                    House house = houseList.get(0);
                    Building building = buildingMapper.selectByPrimaryKey(house.getBuildingId());
                    location += "-";
                    location += (
                            Optional.ofNullable(building.getName()).orElse("")
                            + "-" +
                            Optional.ofNullable(building.getUnitName()).orElse("")
                    );
                    location += "-";
                    location += house.getCard();
                }
                repairDTO.setLocation(location);
                repairDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));
            }
        }
        pageDTO.setList(repairDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存维修信息
     * @param repairDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveRepair(RepairDTO repairDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(repairDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        Repair repair = CopyUtil.copy(repairDTO, Repair.class);
        if(CommonUtil.isEmpty(repair.getId())) {
            // 添加操作
            repair.setId(UuidUtil.getShortUuid());
            repair.setCreateTime(new Date());
            if(repairMapper.insertSelective(repair) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.REPAIR_ADD_ERROR);
            }
        } else {
            // 修改操作
            if(repairMapper.updateByPrimaryKeySelective(repair) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.REPAIR_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存成功！");
    }

    /**
     * 删除维修信息
     * @param repairDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteRepair(RepairDTO repairDTO) {
        if(CommonUtil.isEmpty(repairDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除维修信息
        if(repairMapper.deleteByPrimaryKey(repairDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.REPAIR_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "删除成功！");
    }
}
