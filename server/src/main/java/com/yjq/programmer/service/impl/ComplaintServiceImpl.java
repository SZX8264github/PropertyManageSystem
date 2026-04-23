package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.*;
import com.yjq.programmer.domain.Complaint;
import com.yjq.programmer.domain.ComplaintExample;
import com.yjq.programmer.domain.User;
import com.yjq.programmer.domain.UserExample;
import com.yjq.programmer.dto.ComplaintDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.service.IComplaintService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.CopyUtil;
import com.yjq.programmer.utils.UuidUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-10 9:25
 */
@Service
@Transactional
public class ComplaintServiceImpl implements IComplaintService {

    @Resource
    private ComplaintMapper complaintMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 分页获取投诉数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<ComplaintDTO>> getComplaintList(PageDTO<ComplaintDTO> pageDTO) {
        ComplaintExample complaintExample = new ComplaintExample();
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        // 不知道每页多少条记录，默认为每页5条记录
        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        ComplaintExample.Criteria c1 = complaintExample.createCriteria();
        if(pageDTO.getParam() != null) {
            ComplaintDTO complaintDTO = pageDTO.getParam();
            if(!CommonUtil.isEmpty(complaintDTO.getContent())) {
                c1.andContentLike("%" + complaintDTO.getContent() + "%");
            }
            if(!CommonUtil.isEmpty(complaintDTO.getUserId())) {
                c1.andUserIdEqualTo(complaintDTO.getUserId());
            }
            if(!CommonUtil.isEmpty(complaintDTO.getLoginUserId())) {
                User loginUser = userMapper.selectByPrimaryKey(complaintDTO.getLoginUserId());
                if(RoleEnum.OWNER.getCode().equals(loginUser.getRoleId())) {
                    // 业主只能看到自己的投诉信息
                    c1.andUserIdEqualTo(loginUser.getId());
                } else if (RoleEnum.STAFF.getCode().equals(loginUser.getRoleId())) {
                    // 物业员工只能看到自己小区的投诉信息
                    UserExample userExample = new UserExample();
                    userExample.createCriteria().andDistrictIdEqualTo(loginUser.getDistrictId());
                    List<String> userIdList = userMapper.selectByExample(userExample).stream().map(User::getId).collect(Collectors.toList());
                    c1.andUserIdIn(userIdList);
                }
            }
        }
        complaintExample.setOrderByClause("create_time desc");
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出投诉数据
        List<Complaint> complaintList = complaintMapper.selectByExample(complaintExample);
        PageInfo<Complaint> pageInfo = new PageInfo<>(complaintList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 将domain类型数据  转成 DTO类型数据
        List<ComplaintDTO> complaintDTOList = CopyUtil.copyList(complaintList, ComplaintDTO.class);
        for(ComplaintDTO complaintDTO : complaintDTOList) {
            User user = userMapper.selectByPrimaryKey(complaintDTO.getUserId());
            complaintDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));
        }
        pageDTO.setList(complaintDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存投诉信息
     * @param complaintDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveComplaint(ComplaintDTO complaintDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(complaintDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        Complaint complaint = CopyUtil.copy(complaintDTO, Complaint.class);
        if(CommonUtil.isEmpty(complaint.getId())) {
            // 添加操作
            complaint.setId(UuidUtil.getShortUuid());
            complaint.setCreateTime(new Date());
            if(complaintMapper.insertSelective(complaint) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.COMPLAINT_ADD_ERROR);
            }
        } else {
            // 修改操作
            if(complaintMapper.updateByPrimaryKeySelective(complaint) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.COMPLAINT_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存成功！");
    }

    /**
     * 删除投诉信息
     * @param complaintDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteComplaint(ComplaintDTO complaintDTO) {
        if(CommonUtil.isEmpty(complaintDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除投诉信息
        if(complaintMapper.deleteByPrimaryKey(complaintDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.COMPLAINT_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "删除成功！");
    }
}
