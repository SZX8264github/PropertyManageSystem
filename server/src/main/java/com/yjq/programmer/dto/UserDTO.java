package com.yjq.programmer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yjq.programmer.annotation.ValidateEntity;

import java.util.Date;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-04-15 14:51
 */
public class UserDTO {

    private String id;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=16,minLength=1,errorRequiredMsg="用户姓名不能为空！",errorMaxLengthMsg="用户姓名长度不能大于16！",errorMinLengthMsg="用户姓名不能为空！")
    private String username;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=16,minLength=6,errorRequiredMsg="用户密码不能为空！",errorMaxLengthMsg="用户密码长度不能大于16！",errorMinLengthMsg="用户密码不能小于6！")
    private String password;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=11,minLength=11,errorRequiredMsg="手机号码不能为空！",errorMaxLengthMsg="请输入11位手机号码！",errorMinLengthMsg="请输入11位手机号码！")
    private String phone;

    private Integer roleId;

    private Integer sex;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=32,minLength=1,errorRequiredMsg="身份证号不能为空！",errorMaxLengthMsg="身份证号长度不能大于32！",errorMinLengthMsg="身份证号不能为空！")
    private String identifyCard;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ValidateEntity(required=true,errorRequiredMsg="出生日期不能为空！")
    private Date birthDate;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=128,minLength=1,errorRequiredMsg="职位不能为空！",errorMaxLengthMsg="职位长度不能大于128！",errorMinLengthMsg="职位不能为空！")
    private String position;

    @ValidateEntity(required=true,requiredMaxLength=true,requiredMinLength=true,maxLength=16,minLength=1,errorRequiredMsg="学历不能为空！",errorMaxLengthMsg="学历长度不能大于16！",errorMinLengthMsg="学历不能为空！")
    private String study;

    private String token;

    private String headPic;

    @ValidateEntity(required=true,errorRequiredMsg="所属小区不能为空！",requiredMinLength=true,minLength=1,errorMinLengthMsg="所属小区不能为空！")
    private String districtId;

    private DistrictDTO districtDTO;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getIdentifyCard() {
        return identifyCard;
    }

    public void setIdentifyCard(String identifyCard) {
        this.identifyCard = identifyCard;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStudy() {
        return study;
    }

    public void setStudy(String study) {
        this.study = study;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHeadPic() {
        return headPic;
    }

    public void setHeadPic(String headPic) {
        this.headPic = headPic;
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
