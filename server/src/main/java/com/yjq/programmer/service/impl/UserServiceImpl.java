package com.yjq.programmer.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.*;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.DistrictDTO;
import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.enums.ComplaintStateEnum;
import com.yjq.programmer.enums.RepairStateEnum;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.service.IUserService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.CopyUtil;
import com.yjq.programmer.utils.UuidUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-04-14 21:48
 */
@Service
@Transactional
public class UserServiceImpl implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private DistrictMapper districtMapper;

    @Resource
    private RepairMapper repairMapper;

    @Resource
    private ComplaintMapper complaintMapper;

    @Resource
    private AnnounceMapper announceMapper;

    @Resource
    private BuildingMapper buildingMapper;

    @Resource
    private HouseMapper houseMapper;

    @Resource
    private FeeMapper feeMapper;

    @Resource
    private ParkingMapper parkingMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * 分页获取用户数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<UserDTO>> getUserList(PageDTO<UserDTO> pageDTO) {
        UserExample userExample = new UserExample();
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        // 不知道每页多少条记录，默认为每页5条记录
        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        UserExample.Criteria c1 = userExample.createCriteria();
        if(pageDTO.getParam() != null) {
            UserDTO userDTO = pageDTO.getParam();
            if(userDTO.getRoleId() != null) {
                c1.andRoleIdEqualTo(userDTO.getRoleId());
            }
            if(!CommonUtil.isEmpty(userDTO.getUsername())) {
                c1.andUsernameLike("%" + userDTO.getUsername() + "%");
            }
            if(!CommonUtil.isEmpty(userDTO.getPhone())) {
                c1.andPhoneLike("%" + userDTO.getPhone() + "%");
            }
            // 物业员工只能看自己小区的业主信息  业主只能看到自己小区的物业员工信息
            if(!CommonUtil.isEmpty(userDTO.getDistrictId())) {
                c1.andDistrictIdEqualTo(userDTO.getDistrictId());
            }
            if(!CommonUtil.isEmpty(userDTO.getId())) {
                c1.andIdEqualTo(userDTO.getId());
            }
        }
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出用户数据
        List<User> userList = userMapper.selectByExample(userExample);
        PageInfo<User> pageInfo = new PageInfo<>(userList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 将domain类型数据  转成 DTO类型数据
        List<UserDTO> userDTOList = CopyUtil.copyList(userList, UserDTO.class);
        for(UserDTO userDTO : userDTOList) {
            District district = districtMapper.selectByPrimaryKey(userDTO.getDistrictId());
            userDTO.setDistrictDTO(CopyUtil.copy(district, DistrictDTO.class));
        }
        pageDTO.setList(userDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存用户信息
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveUser(UserDTO userDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(userDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        User user = CopyUtil.copy(userDTO, User.class);
        if(CommonUtil.isEmpty(user.getId())) {
            // 添加操作
            // 判断手机号码是否存在
            if(isPhoneExist(user, "")){
                return ResponseDTO.errorByMsg(CodeMsg.USER_PHONE_EXIST);
            }
            user.setId(UuidUtil.getShortUuid());
            // 密码加密
            user.setPassword(CommonUtil.encryptPassword(user.getPassword()));
            if(userMapper.insertSelective(user) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.USER_ADD_ERROR);
            }
        } else {
            // 如果修改了密码，需要重新加密
            if(!CommonUtil.isEmpty(user.getPassword())) {
                user.setPassword(CommonUtil.encryptPassword(user.getPassword()));
            }
            // 修改操作
            // 判断手机号码是否存在
            if(isPhoneExist(user, user.getId())){
                return ResponseDTO.errorByMsg(CodeMsg.USER_PHONE_EXIST);
            }
            ResponseDTO<UserDTO> loginUser = getLoginUser(userDTO.getToken());
            if(loginUser.getCode() != 0) {
                return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
            }
            if(userMapper.updateByPrimaryKeySelective(user) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.USER_EDIT_ERROR);
            }
            UserDTO loginUserDTO = loginUser.getData();
            if(user.getId().equals(loginUserDTO.getId())) {
                // 如果是修改个人信息  则更新redis中数据
                loginUserDTO = CopyUtil.copy(userMapper.selectByPrimaryKey(user.getId()), UserDTO.class);
                loginUserDTO.setToken(userDTO.getToken());
                stringRedisTemplate.opsForValue().set("USER_" + userDTO.getToken(), JSON.toJSONString(loginUserDTO), 3600, TimeUnit.SECONDS);
            }
        }
        return ResponseDTO.successByMsg(true, "保存成功！");
    }

    /**
     * 删除用户信息
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteUser(UserDTO userDTO) {
        if(CommonUtil.isEmpty(userDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        // 删除用户信息
        if(userMapper.deleteByPrimaryKey(userDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_DELETE_ERROR);
        }
        // 删除用户有关公告信息
        AnnounceExample announceExample = new AnnounceExample();
        announceExample.createCriteria().andUserIdEqualTo(userDTO.getId());
        announceMapper.deleteByExample(announceExample);
        // 删除用户有关维修信息
        RepairExample repairExample = new RepairExample();
        repairExample.createCriteria().andUserIdEqualTo(userDTO.getId());
        repairMapper.deleteByExample(repairExample);
        // 删除用户有关投诉信息
        ComplaintExample complaintExample = new ComplaintExample();
        complaintExample.createCriteria().andUserIdEqualTo(userDTO.getId());
        complaintMapper.deleteByExample(complaintExample);
        // 删除用户有关缴费信息
        FeeExample feeExample = new FeeExample();
        feeExample.createCriteria().andUserIdEqualTo(userDTO.getId());
        feeMapper.deleteByExample(feeExample);
        // 重置用户有关车位信息
        ParkingExample parkingExample = new ParkingExample();
        parkingExample.createCriteria().andUserIdEqualTo(userDTO.getId());
        Parking parking = new Parking();
        parking.setUserId("");
        parkingMapper.updateByExampleSelective(parking, parkingExample);
        // 重置用户有关的房屋信息
        HouseExample houseExample = new HouseExample();
        houseExample.createCriteria().andUserIdEqualTo(userDTO.getId());
        House house = new House();
        house.setUserId("");
        houseMapper.updateByExampleSelective(house, houseExample);
        return ResponseDTO.successByMsg(true, "删除成功！");
    }

    /**
     * 用户登录操作
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<UserDTO> login(UserDTO userDTO) {
        // 进行是否为空判断
        if(CommonUtil.isEmpty(userDTO.getPhone())){
            return ResponseDTO.errorByMsg(CodeMsg.PHONE_EMPTY);
        }
        if(CommonUtil.isEmpty(userDTO.getPassword())){
            return ResponseDTO.errorByMsg(CodeMsg.PASSWORD_EMPTY);
        }
        // 对比昵称和密码是否正确
        UserExample userExample = new UserExample();
        userExample.createCriteria().andPhoneEqualTo(userDTO.getPhone()).andPasswordEqualTo(userDTO.getPassword()).andRoleIdEqualTo(userDTO.getRoleId());
        List<User> userList = userMapper.selectByExample(userExample);
        if(userList == null || userList.size() != 1){
            return ResponseDTO.errorByMsg(CodeMsg.PHONE_PASSWORD_ERROR);
        }
        // 生成登录token并存入Redis中
        UserDTO selectedUserDTO = CopyUtil.copy(userList.get(0), UserDTO.class);
        String token = UuidUtil.getShortUuid();
        selectedUserDTO.setToken(token);
        //把token存入redis中 有效期1小时
        stringRedisTemplate.opsForValue().set("USER_" + token, JSON.toJSONString(selectedUserDTO), 3600, TimeUnit.SECONDS);
        return ResponseDTO.successByMsg(selectedUserDTO, "登录成功！");
    }

    /**
     * 用户退出登录
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> logout(UserDTO userDTO) {
        if(!CommonUtil.isEmpty(userDTO.getToken())){
            // token不为空  清除redis中数据
            stringRedisTemplate.delete("USER_" + userDTO.getToken());
        }
        return ResponseDTO.successByMsg(true, "退出登录成功！");
    }

    /**
     * 检查用户是否登录
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<UserDTO> checkLogin(UserDTO userDTO) {
        if(userDTO == null || CommonUtil.isEmpty(userDTO.getToken())){
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        ResponseDTO<UserDTO> responseDTO = getLoginUser(userDTO.getToken());
        if(responseDTO.getCode() != 0){
            return responseDTO;
        }
        logger.info("获取到的登录信息={}", responseDTO.getData());
        return ResponseDTO.success(responseDTO.getData());
    }

    /**
     * 根据条件查询所有用户信息
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<List<UserDTO>> getAllUserList(UserDTO userDTO) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if(userDTO.getRoleId() != null) {
            criteria.andRoleIdEqualTo(userDTO.getRoleId());
        }
        if(!CommonUtil.isEmpty(userDTO.getDistrictId())) {
            criteria.andDistrictIdEqualTo(userDTO.getDistrictId());
        }
        List<User> userList = userMapper.selectByExample(userExample);
        return ResponseDTO.success(CopyUtil.copyList(userList, UserDTO.class));
    }

    /**
     * 系统首页总数统计
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Map<String, Object>> getTotalStatistic(UserDTO userDTO) {
        Map<String, Object> resultMap = new HashMap<>();
        int total1 = 0;
        int total2 = 0;
        int total3 = 0;
        int total4 = 0;
        if(RoleEnum.OWNER.getCode().equals(userDTO.getRoleId())) {
            // 业主视角下的数据统计
            RepairExample repairExample = new RepairExample();
            repairExample.createCriteria().andUserIdEqualTo(userDTO.getId());
            List<Repair> repairList = repairMapper.selectByExample(repairExample);
            total1 = (int) repairList.stream().filter(e -> e.getState().equals(RepairStateEnum.WAIT.getCode())).count();
            total2 = (int) repairList.stream().filter(e -> e.getState().equals(RepairStateEnum.FINISH.getCode())).count();
            ComplaintExample complaintExample = new ComplaintExample();
            complaintExample.createCriteria().andUserIdEqualTo(userDTO.getId());
            List<Complaint> complainList = complaintMapper.selectByExample(complaintExample);
            total3 = (int) complainList.stream().filter(e -> e.getState().equals(ComplaintStateEnum.WAIT.getCode())).count();
            total4 = (int) complainList.stream().filter(e -> e.getState().equals(ComplaintStateEnum.FINISH.getCode())).count();

        } else if (RoleEnum.STAFF.getCode().equals(userDTO.getRoleId())) {
            // 物业员工视角下的数据统计
            UserExample userExample = new UserExample();
            userExample.createCriteria().andRoleIdEqualTo(RoleEnum.OWNER.getCode()).andDistrictIdEqualTo(userDTO.getDistrictId());
            total1 = userMapper.countByExample(userExample);
            UserExample userExample2 = new UserExample();
            UserExample.Criteria criteria1 = userExample2.createCriteria();
            criteria1.andDistrictIdEqualTo(userDTO.getDistrictId());
            List<String> userIdList = userMapper.selectByExample(userExample2).stream().map(User::getId).collect(Collectors.toList());

            RepairExample repairExample = new RepairExample();
            ComplaintExample complaintExample = new ComplaintExample();
            if(userIdList.size() > 0) {
                repairExample.createCriteria().andUserIdIn(userIdList).andStateEqualTo(RepairStateEnum.WAIT.getCode());
                complaintExample.createCriteria().andUserIdIn(userIdList).andStateEqualTo(ComplaintStateEnum.WAIT.getCode());
            } else {
                repairExample.createCriteria().andUserIdEqualTo("-").andStateEqualTo(RepairStateEnum.WAIT.getCode());
                complaintExample.createCriteria().andUserIdEqualTo("-").andStateEqualTo(ComplaintStateEnum.WAIT.getCode());
            }
            total2 = repairMapper.countByExample(repairExample);
            total3 = complaintMapper.countByExample(complaintExample);
            AnnounceExample announceExample = new AnnounceExample();

            UserExample.Criteria criteria2 = userExample2.createCriteria();
            criteria2.andRoleIdEqualTo(RoleEnum.ADMIN.getCode());
            userExample2.or(criteria2);
            List<String> userIdList2 = userMapper.selectByExample(userExample2).stream().map(User::getId).collect(Collectors.toList());
            announceExample.createCriteria().andUserIdIn(userIdList2);
            total4 = announceMapper.countByExample(announceExample);
        } else if (RoleEnum.ADMIN.getCode().equals(userDTO.getRoleId())) {
            total1 = districtMapper.countByExample(new DistrictExample());
            UserExample userExample = new UserExample();
            userExample.createCriteria().andRoleIdEqualTo(RoleEnum.STAFF.getCode());
            total2 = userMapper.countByExample(userExample);
            total3 = buildingMapper.countByExample(new BuildingExample());
            total4 = houseMapper.countByExample(new HouseExample());
        }
        resultMap.put("total1", total1);
        resultMap.put("total2", total2);
        resultMap.put("total3", total3);
        resultMap.put("total4", total4);
        return ResponseDTO.success(resultMap);
    }

    /**
     * 获取当前登录用户
     * @return
     */
    public ResponseDTO<UserDTO> getLoginUser(String token){
        String value = stringRedisTemplate.opsForValue().get("USER_" + token);
        if(CommonUtil.isEmpty(value)){
            return ResponseDTO.errorByMsg(CodeMsg.USER_SESSION_EXPIRED);
        }
        UserDTO selectedUserDTO = JSON.parseObject(value, UserDTO.class);
        return ResponseDTO.success(selectedUserDTO);
    }

    /**
     * 判断用户手机号是否重复
     * @param user
     * @param id
     * @return
     */
    public Boolean isPhoneExist(User user, String id) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andPhoneEqualTo(user.getPhone());
        List<User> selectedUserList = userMapper.selectByExample(userExample);
        if (selectedUserList != null && selectedUserList.size() > 0) {
            if (selectedUserList.size() > 1) {
                return true; //出现重复
            }
            if (!selectedUserList.get(0).getId().equals(id)) {
                return true; //出现重复
            }
        }
        return false;//没有重复
    }

}
