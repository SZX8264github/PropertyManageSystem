package com.yjq.programmer.enums;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-06-05 14:52
 */
public enum FeeStateEnum {

    WAIT(1,"待支付"),

    PAYED(2,"已支付"),

    OUT(3,"已逾期"),

    ;

    Integer code;

    String desc;

    FeeStateEnum(Integer code, String desc) {
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
