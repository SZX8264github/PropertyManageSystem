package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.AnnounceMapper;
import com.yjq.programmer.dao.UserMapper;
import com.yjq.programmer.domain.Announce;
import com.yjq.programmer.domain.AnnounceExample;
import com.yjq.programmer.domain.User;
import com.yjq.programmer.domain.UserExample;
import com.yjq.programmer.dto.AnnounceDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.service.IAnnounceService;
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
 * @create 2024-06-08 21:13
 */
@Service
@Transactional
public class AnnounceServiceImpl implements IAnnounceService {

    @Resource
    private AnnounceMapper announceMapper;

    @Resource
    private UserMapper userMapper;

    /**
     * 分页获取公告数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<AnnounceDTO>> getAnnounceList(PageDTO<AnnounceDTO> pageDTO) {
        AnnounceExample announceExample = new AnnounceExample();
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        // 不知道每页多少条记录，默认为每页5条记录
        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        AnnounceExample.Criteria c1 = announceExample.createCriteria();
        if(pageDTO.getParam() != null) {
            AnnounceDTO announceDTO = pageDTO.getParam();
            if(!CommonUtil.isEmpty(announceDTO.getContent())) {
                c1.andContentLike("%" + announceDTO.getContent() + "%");
            }
            if(!CommonUtil.isEmpty(announceDTO.getUserId())) {
                User user = userMapper.selectByPrimaryKey(announceDTO.getUserId());
                if(RoleEnum.OWNER.getCode().equals(user.getRoleId()) || RoleEnum.STAFF.getCode().equals(user.getRoleId())) {
                    // 业主和物业只能看到自己小区物业公告和管理员的公告
                    UserExample userExample = new UserExample();
                    UserExample.Criteria criteria1 = userExample.createCriteria();
                    criteria1.andDistrictIdEqualTo(user.getDistrictId());
                    UserExample.Criteria criteria2 = userExample.createCriteria();
                    criteria2.andRoleIdEqualTo(RoleEnum.ADMIN.getCode());
                    userExample.or(criteria2);
                    List<String> userIdList = userMapper.selectByExample(userExample).stream().map(User::getId).collect(Collectors.toList());
                    c1.andUserIdIn(userIdList);
                }
            }
        }
        announceExample.setOrderByClause("top desc, create_time desc");
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出公告数据
        List<Announce> announceList = announceMapper.selectByExample(announceExample);
        PageInfo<Announce> pageInfo = new PageInfo<>(announceList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 将domain类型数据  转成 DTO类型数据
        List<AnnounceDTO> announceDTOList = CopyUtil.copyList(announceList, AnnounceDTO.class);
        for(AnnounceDTO announceDTO : announceDTOList) {
            User user = userMapper.selectByPrimaryKey(announceDTO.getUserId());
            announceDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));
            if(announceDTO.getTop() == 2) {
                announceDTO.setTop(1);
            }
        }
        pageDTO.setList(announceDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存公告信息
     * @param announceDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveAnnounce(AnnounceDTO announceDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(announceDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        Announce announce = CopyUtil.copy(announceDTO, Announce.class);
        if(CommonUtil.isEmpty(announce.getId())) {
            // 添加操作
            announce.setId(UuidUtil.getShortUuid());
            announce.setCreateTime(new Date());
            if(announceMapper.insertSelective(announce) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.ANNOUNCE_ADD_ERROR);
            }
        } else {
            // 修改操作
            if(announceMapper.updateByPrimaryKeySelective(announce) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.ANNOUNCE_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存成功！");
    }

    /**
     * 删除公告信息
     * @param announceDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteAnnounce(AnnounceDTO announceDTO) {
        if(CommonUtil.isEmpty(announceDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除公告信息
        if(announceMapper.deleteByPrimaryKey(announceDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.ANNOUNCE_DELETE_ERROR);
        }
        return ResponseDTO.successByMsg(true, "删除成功！");
    }
}
