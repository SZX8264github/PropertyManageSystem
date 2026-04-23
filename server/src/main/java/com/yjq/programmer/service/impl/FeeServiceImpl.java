package com.yjq.programmer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiaogang.xxljobadminsdk.model.DefaultXxlJobAddParam;
import com.xiaogang.xxljobadminsdk.service.XxlJobService;
import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dao.*;
import com.yjq.programmer.dao.my.MyFeeMapper;
import com.yjq.programmer.domain.*;
import com.yjq.programmer.dto.*;
import com.yjq.programmer.enums.FeeStateEnum;
import com.yjq.programmer.enums.RoleEnum;
import com.yjq.programmer.service.IFeeService;
import com.yjq.programmer.utils.CommonUtil;
import com.yjq.programmer.utils.CopyUtil;
import com.yjq.programmer.utils.UuidUtil;
import com.yjq.programmer.utils.ValidateEntityUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-05 14:46
 */
@Service
@Transactional
public class FeeServiceImpl implements IFeeService {

    @Resource
    private FeeMapper feeMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private DistrictMapper districtMapper;

    @Resource
    private MyFeeMapper myFeeMapper;

    @Resource
    private XxlJobService xxlJobService;


    /**
     * 分页获取缴费数据
     * @param pageDTO
     * @return
     */
    @Override
    public ResponseDTO<PageDTO<FeeDTO>> getFeeList(PageDTO<FeeDTO> pageDTO) {
        FeeExample feeExample = new FeeExample();
        // 不知道当前页多少，默认为第一页
        if(pageDTO.getPage() == null){
            pageDTO.setPage(1);
        }
        // 不知道每页多少条记录，默认为每页5条记录
        if(pageDTO.getSize() == null){
            pageDTO.setSize(5);
        }
        FeeExample.Criteria c1 = feeExample.createCriteria();
        if(pageDTO.getParam() != null) {
            FeeDTO feeDTO = pageDTO.getParam();
            if(!CommonUtil.isEmpty(feeDTO.getContent())) {
                c1.andContentLike("%" + feeDTO.getContent() + "%");
            }
            if(!CommonUtil.isEmpty(feeDTO.getUserId())) {
                c1.andUserIdEqualTo(feeDTO.getUserId());
            }
            if(!CommonUtil.isEmpty(feeDTO.getDistrictId())) {
                // 物业员工只能看到自己小区的缴费信息
                UserExample userExample = new UserExample();
                userExample.createCriteria().andDistrictIdEqualTo(feeDTO.getDistrictId());
                List<String> userIdList = userMapper.selectByExample(userExample).stream().map(User::getId).collect(Collectors.toList());
                if(userIdList.size() > 0) {
                    c1.andUserIdIn(userIdList);
                } else {
                    c1.andUserIdEqualTo("-");
                }
            }
        }
        feeExample.setOrderByClause("create_time desc");
        PageHelper.startPage(pageDTO.getPage(), pageDTO.getSize());
        // 分页查出缴费数据
        List<Fee> feeList = feeMapper.selectByExample(feeExample);
        PageInfo<Fee> pageInfo = new PageInfo<>(feeList);
        // 获取数据的总数
        pageDTO.setTotal(pageInfo.getTotal());
        // 将domain类型数据  转成 DTO类型数据
        List<FeeDTO> feeDTOList = CopyUtil.copyList(feeList, FeeDTO.class);
        for(FeeDTO feeDTO : feeDTOList) {
            User user = userMapper.selectByPrimaryKey(feeDTO.getUserId());
            feeDTO.setUserDTO(CopyUtil.copy(user, UserDTO.class));
            District district = districtMapper.selectByPrimaryKey(user.getDistrictId());
            feeDTO.setDistrictDTO(CopyUtil.copy(district, DistrictDTO.class));
        }
        pageDTO.setList(feeDTOList);
        return ResponseDTO.success(pageDTO);
    }

    /**
     * 保存缴费信息
     * @param feeDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> saveFee(FeeDTO feeDTO) {
        // 进行统一表单验证
        CodeMsg validate = ValidateEntityUtil.validate(feeDTO);
        if (!validate.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            return ResponseDTO.errorByMsg(validate);
        }
        Fee fee = CopyUtil.copy(feeDTO, Fee.class);
        User user = userMapper.selectByPrimaryKey(fee.getUserId());
        if(user == null) {
            return ResponseDTO.errorByMsg(CodeMsg.USER_NOT_EXIST);
        }
        // 准备创建xxl-job任务
        DefaultXxlJobAddParam defaultXxlJobAddParam = new DefaultXxlJobAddParam();
        defaultXxlJobAddParam.setAuthor("杨杨吖");
        defaultXxlJobAddParam.setJobDesc("缴费逾期罚金处理任务");
        defaultXxlJobAddParam.setExecutorHandler("feeHandler");
        Calendar calendar = Calendar.getInstance();
        if(fee.getDeadTime() != null) {
            calendar.setTime(fee.getDeadTime());
            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            defaultXxlJobAddParam.setScheduleConf("0/0 0/0 0/0 " + day + "/1 * ?");
            defaultXxlJobAddParam.setScheduleConf("0/0 0/0 0/0 * * ?");
        }
        if(CommonUtil.isEmpty(fee.getId())) {
            // 添加操作
            fee.setId(UuidUtil.getShortUuid());
            fee.setCreateTime(new Date());
            if(fee.getDeadTime() != null) {
                defaultXxlJobAddParam.setExecutorParam(fee.getId());
                Integer taskId = xxlJobService.add(defaultXxlJobAddParam);
                xxlJobService.start(taskId);
                fee.setTaskId(String.valueOf(taskId));
            }
            if(feeMapper.insertSelective(fee) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.FEE_ADD_ERROR);
            }
        } else {
            // 修改操作
            Fee feeDB = feeMapper.selectByPrimaryKey(fee.getId());
            if(!FeeStateEnum.PAYED.getCode().equals(feeDB.getState()) && FeeStateEnum.PAYED.getCode().equals(fee.getState())) {
                fee.setPayTime(new Date());
                xxlJobService.remove(Integer.parseInt(feeDB.getTaskId()));
            }
            if(feeDB.getDeadTime().getTime() != fee.getDeadTime().getTime()) {
                defaultXxlJobAddParam.setExecutorParam(feeDB.getId());
                Integer taskId = xxlJobService.add(defaultXxlJobAddParam);
                xxlJobService.start(taskId);
                fee.setTaskId(String.valueOf(taskId));
            }
            if(feeMapper.updateByPrimaryKeySelective(fee) == 0) {
                return ResponseDTO.errorByMsg(CodeMsg.FEE_EDIT_ERROR);
            }
        }
        return ResponseDTO.successByMsg(true, "保存成功！");
    }

    /**
     * 删除缴费信息
     * @param feeDTO
     * @return
     */
    @Override
    public ResponseDTO<Boolean> deleteFee(FeeDTO feeDTO) {
        if(CommonUtil.isEmpty(feeDTO.getId())) {
            return ResponseDTO.errorByMsg(CodeMsg.DATA_ERROR);
        }
        Fee feeDB = feeMapper.selectByPrimaryKey(feeDTO.getId());
        if(feeDB == null) {
            return ResponseDTO.successByMsg(true, "删除成功！");
        }
        // 删除缴费信息
        if(feeMapper.deleteByPrimaryKey(feeDTO.getId()) == 0) {
            return ResponseDTO.errorByMsg(CodeMsg.FEE_DELETE_ERROR);
        }
        xxlJobService.remove(Integer.parseInt(feeDB.getTaskId()));
        return ResponseDTO.successByMsg(true, "删除成功！");
    }

    /**
     * 获取近五个月缴费数据
     * @param userDTO
     * @return
     */
    @Override
    public ResponseDTO<Map<String, Object>> getFeeStatistic(UserDTO userDTO) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Integer> payedList = new ArrayList<>();
        List<Integer> noPayedList = new ArrayList<>();
        Map<String, Object> queryMap = new HashMap<>();
        List<Integer> payStateList = new ArrayList<>();
        payStateList.add(2);
        List<Integer> noPayStateList = new ArrayList<>();
        noPayStateList.add(1);
        noPayStateList.add(3);
        List<String> userIdList = new ArrayList<>();
        if(RoleEnum.OWNER.getCode().equals(userDTO.getRoleId())) {
            // 业主只能看到自己的缴费信息
            userIdList.add(userDTO.getId());
        } else if (RoleEnum.STAFF.getCode().equals(userDTO.getRoleId())) {
            // 物业员工能看自己小区的缴费信息
            UserExample userExample = new UserExample();
            userExample.createCriteria().andDistrictIdEqualTo(userDTO.getDistrictId());
            userIdList = userMapper.selectByExample(userExample).stream().map(User::getId).collect(Collectors.toList());
        }
        queryMap.put("userIdList", userIdList);
        // 查询当月数据
        queryMap.put("interval", 0);
        payedList.add(myFeeMapper.getFeeTotalByDateAndState(queryMap, payStateList));
        noPayedList.add(myFeeMapper.getFeeTotalByDateAndState(queryMap, noPayStateList));
        // 查询上个月数据
        queryMap.put("interval", 1);
        payedList.add(myFeeMapper.getFeeTotalByDateAndState(queryMap, payStateList));
        noPayedList.add(myFeeMapper.getFeeTotalByDateAndState(queryMap, noPayStateList));
        // 查询上上个月数据
        queryMap.put("interval", 2);
        payedList.add(myFeeMapper.getFeeTotalByDateAndState(queryMap, payStateList));
        noPayedList.add(myFeeMapper.getFeeTotalByDateAndState(queryMap, noPayStateList));
        // 查询上上上个月数据
        queryMap.put("interval", 3);
        payedList.add(myFeeMapper.getFeeTotalByDateAndState(queryMap, payStateList));
        noPayedList.add(myFeeMapper.getFeeTotalByDateAndState(queryMap, noPayStateList));
        // 查询上上上上个月数据
        queryMap.put("interval", 4);
        payedList.add(myFeeMapper.getFeeTotalByDateAndState(queryMap, payStateList));
        noPayedList.add(myFeeMapper.getFeeTotalByDateAndState(queryMap, noPayStateList));
        resultMap.put("payedList", payedList);
        resultMap.put("noPayedList", noPayedList);
        return ResponseDTO.success(resultMap);
    }

}
