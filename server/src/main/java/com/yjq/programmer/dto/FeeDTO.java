package com.yjq.programmer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yjq.programmer.annotation.ValidateEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-05 14:32
 */
public class FeeDTO {

    private String id;

    private String userId;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=512,minLength=1,errorRequiredMsg="缴费内容不能为空！",errorMaxLengthMsg="缴费内容长度不能大于512！",errorMinLengthMsg="缴费内容不能为空！")
    private String content;

    private String districtId;

    private Date createTime;

    private Integer state;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date deadTime;

    private Date payTime;

    @ValidateEntity(required=true,requiredMaxValue=true,requiredMinValue=true,maxValue=99999999.99,minValue=0.00,errorRequiredMsg="缴费金额不能为空！",errorMaxValueMsg="缴费金额不能大于99999999.99元！",errorMinValueMsg="缴费金额不能小于0.00元！")
    private BigDecimal price;

    @ValidateEntity(required=true,requiredMaxValue=true,requiredMinValue=true,maxValue=99999999.99,minValue=0.00,errorRequiredMsg="逾期附加费用不能为空！",errorMaxValueMsg="逾期附加费用不能大于99999999.99元！",errorMinValueMsg="逾期附加费用不能小于0.00元！")
    private BigDecimal deadPrice;

    private DistrictDTO districtDTO;

    private UserDTO userDTO;

    private String houseName;

    private String taskId;

    private BigDecimal addPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(Date deadTime) {
        this.deadTime = deadTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getAddPrice() {
        return addPrice;
    }

    public void setAddPrice(BigDecimal addPrice) {
        this.addPrice = addPrice;
    }

    public DistrictDTO getDistrictDTO() {
        return districtDTO;
    }

    public void setDistrictDTO(DistrictDTO districtDTO) {
        this.districtDTO = districtDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public BigDecimal getDeadPrice() {
        return deadPrice;
    }

    public void setDeadPrice(BigDecimal deadPrice) {
        this.deadPrice = deadPrice;
    }
}
