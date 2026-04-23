package com.yjq.programmer.dto;

import com.yjq.programmer.annotation.ValidateEntity;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-03 13:35
 */
public class BuildingDTO {

    private String id;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=32,minLength=1,errorRequiredMsg="楼栋名称不能为空！",errorMaxLengthMsg="楼栋名称长度不能大于32！",errorMinLengthMsg="楼栋名称不能为空！")
    private String name;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=32,minLength=1,errorRequiredMsg="单元名称不能为空！",errorMaxLengthMsg="单元名称长度不能大于32！",errorMinLengthMsg="单元名称不能为空！")
    private String unitName;

    @ValidateEntity(required=true,errorRequiredMsg="所属小区不能为空！",requiredMinLength=true,minLength=1,errorMinLengthMsg="所属小区不能为空！")
    private String districtId;

    private DistrictDTO districtDTO;

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

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public DistrictDTO getDistrictDTO() {
        return districtDTO;
    }

    public void setDistrictDTO(DistrictDTO districtDTO) {
        this.districtDTO = districtDTO;
    }
}
