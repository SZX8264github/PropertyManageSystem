package com.yjq.programmer.enums;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-05-06 20:25
 */
public enum RoleEnum {

    OWNER(1,"业主"),

    STAFF(2,"物业员工"),

    ADMIN(3,"管理员"),

    ;

    Integer code;

    String desc;

    RoleEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
