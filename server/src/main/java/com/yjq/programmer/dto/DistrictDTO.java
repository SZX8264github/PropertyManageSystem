package com.yjq.programmer.dto;

import com.yjq.programmer.annotation.ValidateEntity;

import java.math.BigDecimal;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-02 10:50
 */
public class DistrictDTO {

    private String id;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=32,minLength=1,errorRequiredMsg="小区名称不能为空！",errorMaxLengthMsg="小区名称长度不能大于32！",errorMinLengthMsg="小区名称不能为空！")
    private String name;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=128,minLength=1,errorRequiredMsg="小区地址不能为空！",errorMaxLengthMsg="小区地址长度不能大于128！",errorMinLengthMsg="小区地址不能为空！")
    private String location;

    @ValidateEntity(required=true,requiredMaxValue=true,requiredMinValue=true,maxValue=99999999.99,minValue=0.00,errorRequiredMsg="占地面积不能为空！",errorMaxValueMsg="占地面积不能大于99999999.99㎡！",errorMinValueMsg="占地面积不能小于0.00㎡！")
    private BigDecimal area;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=128,minLength=1,errorRequiredMsg="开发商名称不能为空！",errorMaxLengthMsg="开发商名称长度不能大于128！",errorMinLengthMsg="开发商名称不能为空！")
    private String devName;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=128,minLength=1,errorRequiredMsg="物业公司名称不能为空！",errorMaxLengthMsg="物业公司名称长度不能大于128！",errorMinLengthMsg="物业公司名称不能为空！")
    private String propertyName;

    private String photo;

    private Integer buildingTotal;

    private Integer houseTotal;

    private Integer parkingTotal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getBuildingTotal() {
        return buildingTotal;
    }

    public void setBuildingTotal(Integer buildingTotal) {
        this.buildingTotal = buildingTotal;
    }

    public Integer getHouseTotal() {
        return houseTotal;
    }

    public void setHouseTotal(Integer houseTotal) {
        this.houseTotal = houseTotal;
    }

    public Integer getParkingTotal() {
        return parkingTotal;
    }

    public void setParkingTotal(Integer parkingTotal) {
        this.parkingTotal = parkingTotal;
    }
}
