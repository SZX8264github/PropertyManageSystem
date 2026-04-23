package com.yjq.programmer.dto;

import com.yjq.programmer.annotation.ValidateEntity;

import java.math.BigDecimal;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-03 16:28
 */
public class HouseDTO {

    private String id;

    @ValidateEntity(required=true,errorRequiredMsg="所属楼栋不能为空！",requiredMinLength=true,minLength=1,errorMinLengthMsg="所属楼栋不能为空！")
    private String buildingId;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=8,minLength=1,errorRequiredMsg="门牌号不能为空！",errorMaxLengthMsg="门牌号长度不能大于8！",errorMinLengthMsg="门牌号不能为空！")
    private String card;

    @ValidateEntity(required=true,requiredMaxValue=true,requiredMinValue=true,maxValue=99999999.99,minValue=0.00,errorRequiredMsg="房屋面积不能为空！",errorMaxValueMsg="房屋面积不能大于99999999.99㎡！",errorMinValueMsg="房屋面积不能小于0.00㎡！")
    private BigDecimal area;

    private String userId;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=128,minLength=1,errorRequiredMsg="房屋详细信息不能为空！",errorMaxLengthMsg="房屋详细信息长度不能大于128！",errorMinLengthMsg="房屋详细信息不能为空！")
    private String content;

    private BuildingDTO buildingDTO;

    private UserDTO userDTO;

    private String districtId;

    @ValidateEntity(required=true,requiredMaxValue=true,requiredMinValue=true,maxValue=99999999,minValue=1,errorRequiredMsg="楼层不能为空！",errorMaxValueMsg="楼层不能大于99999999！",errorMinValueMsg="楼层不能小于1！")
    private Integer floor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
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

    public BuildingDTO getBuildingDTO() {
        return buildingDTO;
    }

    public void setBuildingDTO(BuildingDTO buildingDTO) {
        this.buildingDTO = buildingDTO;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }
}
