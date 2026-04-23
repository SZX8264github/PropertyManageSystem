package com.yjq.programmer.bean;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2020-09-19 23:14
 */

/**
 * 错误码统一处理类，所有的错误码统一定义在这里
 */
public class CodeMsg {

    private Integer code;//错误码

    private String msg;//错误信息

    /**
     * 构造函数私有化即单例模式
     * 该类负责创建自己的对象，同时确保只有单个对象被创建。这个类提供了一种访问其唯一的对象的方式，可以直接访问，不需要实例化该类的对象。
     * @param code
     * @param msg
     */
    private CodeMsg(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg() {

    }

    public Integer getCode() {
        return code;
    }



    public void setCode(Integer code) {
        this.code = code;
    }



    public String getMsg() {
        return msg;
    }



    public void setMsg(String msg) {
        this.msg = msg;
    }

    //通用错误码定义
    //处理成功消息码
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    //通用数据错误码
    public static CodeMsg DATA_ERROR = new CodeMsg(-1, "非法数据！");
    public static CodeMsg VALIDATE_ENTITY_ERROR = new CodeMsg(-2, "");
    public static CodeMsg CAPTCHA_EMPTY = new CodeMsg(-3, "验证码不能为空!");
    public static CodeMsg NO_PERMISSION = new CodeMsg(-4, "您没有当前操作的权限哦！");
    public static CodeMsg CAPTCHA_ERROR = new CodeMsg(-5, "验证码错误！");
    public static CodeMsg USER_SESSION_EXPIRED = new CodeMsg(-6, "还未登录或会话失效，请重新登录！");
    public static CodeMsg UPLOAD_PHOTO_SUFFIX_ERROR = new CodeMsg(-7, "图片格式不正确！");
    public static CodeMsg PHOTO_SURPASS_MAX_SIZE = new CodeMsg(-8, "上传的图片不能超过2MB！");
    public static CodeMsg PHOTO_FORMAT_NOT_CORRECT = new CodeMsg(-9, "上传的图片格式不正确！");
    public static CodeMsg SAVE_FILE_EXCEPTION = new CodeMsg(-10, "保存文件异常！");
    public static CodeMsg FILE_EXPORT_ERROR = new CodeMsg(-11, "文件导出失败！");
    public static CodeMsg SYSTEM_ERROR = new CodeMsg(-12, "系统出现了错误，请联系管理员！");
    public static CodeMsg NO_AUTHORITY = new CodeMsg(-13, "不好意思，您没有权限操作哦！");
    public static CodeMsg CAPTCHA_EXPIRED = new CodeMsg(-14, "验证码已过期，请刷新验证码！");
    public static CodeMsg COMMON_ERROR = new CodeMsg(-15, "");
    public static CodeMsg PHOTO_EMPTY = new CodeMsg(-16, "上传的图片不能为空！");


    //用户管理类错误码
    public static CodeMsg USER_ADD_ERROR = new CodeMsg(-1000, "用户信息添加失败，请联系管理员！");
    public static CodeMsg USER_NOT_EXIST  = new CodeMsg(-1001, "该用户不存在！");
    public static CodeMsg USER_EDIT_ERROR = new CodeMsg(-1002, "用户信息编辑失败，请联系管理员！");
    public static CodeMsg USER_DELETE_ERROR = new CodeMsg(-1003, "用户信息删除失败，请联系管理员！");
    public static CodeMsg USER_PHONE_EXIST = new CodeMsg(-1004, "用户手机号码已经注册，请换一个！");
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(-1006, "用户密码不能为空！");
    public static CodeMsg DISTRICT_MANAGE_EMPTY = new CodeMsg(-1007, "所属小区不能为空！");
    public static CodeMsg REPASSWORD_EMPTY = new CodeMsg(-1008, "确认密码不能为空！");
    public static CodeMsg REPASSWORD_ERROR = new CodeMsg(-1009, "确认密码不一致！");
    public static CodeMsg USER_REGISTER_ERROR = new CodeMsg(-1010, "注册用户失败，请联系管理员！");
    public static CodeMsg USER_NOT_IS_ADMIN = new CodeMsg(-1011, "只有管理员角色才能登录后台系统！");
    public static CodeMsg USER_NAME_EXIST = new CodeMsg(-1012, "用户昵称重复，请换一个！");
    public static CodeMsg PHONE_PASSWORD_ERROR = new CodeMsg(-1013, "手机号或密码错误！");
    public static CodeMsg PHONE_EMPTY = new CodeMsg(-1014, "手机号码不能为空！");

    //小区管理类错误码
    public static CodeMsg DISTRICT_ADD_ERROR = new CodeMsg(-2000, "小区信息添加失败，请联系管理员！");
    public static CodeMsg DISTRICT_EDIT_ERROR = new CodeMsg(-2001, "小区信息编辑失败，请联系管理员！");
    public static CodeMsg DISTRICT_DELETE_ERROR = new CodeMsg(-2002, "小区信息删除失败，请联系管理员！");

    //楼栋管理类错误码
    public static CodeMsg BUILDING_ADD_ERROR = new CodeMsg(-3000, "楼栋信息添加失败，请联系管理员！");
    public static CodeMsg BUILDING_EDIT_ERROR = new CodeMsg(-3001, "楼栋信息编辑失败，请联系管理员！");
    public static CodeMsg BUILDING_DELETE_ERROR = new CodeMsg(-3002, "楼栋信息删除失败，请联系管理员！");
    public static CodeMsg BUILDING_EXIST = new CodeMsg(-3003, "此小区楼栋信息存在，请换一个！");
    public static CodeMsg HOUSE_ADD_ERROR = new CodeMsg(-3004, "房屋信息添加失败，请联系管理员！");
    public static CodeMsg HOUSE_EDIT_ERROR = new CodeMsg(-3005, "房屋信息编辑失败，请联系管理员！");
    public static CodeMsg HOUSE_DELETE_ERROR = new CodeMsg(-3006, "房屋信息删除失败，请联系管理员！");
    public static CodeMsg HOUSE_EXIST = new CodeMsg(-3007, "此楼栋的房屋信息存在，请换一个！");

    //车位管理类错误码
    public static CodeMsg PARKING_ADD_ERROR = new CodeMsg(-4000, "车位信息添加失败，请联系管理员！");
    public static CodeMsg PARKING_EDIT_ERROR = new CodeMsg(-4001, "车位信息编辑失败，请联系管理员！");
    public static CodeMsg PARKING_DELETE_ERROR = new CodeMsg(-4002, "车位信息删除失败，请联系管理员！");

    //缴费管理类错误码
    public static CodeMsg FEE_ADD_ERROR = new CodeMsg(-5000, "缴费信息添加失败，请联系管理员！");
    public static CodeMsg FEE_EDIT_ERROR = new CodeMsg(-5001, "缴费信息编辑失败，请联系管理员！");
    public static CodeMsg FEE_DELETE_ERROR = new CodeMsg(-5002, "缴费信息删除失败，请联系管理员！");

    //公告管理类错误码
    public static CodeMsg ANNOUNCE_ADD_ERROR = new CodeMsg(-6000, "公告信息添加失败，请联系管理员！");
    public static CodeMsg ANNOUNCE_EDIT_ERROR = new CodeMsg(-6001, "公告信息编辑失败，请联系管理员！");
    public static CodeMsg ANNOUNCE_DELETE_ERROR = new CodeMsg(-6002, "公告信息删除失败，请联系管理员！");

    //维修管理类错误码
    public static CodeMsg REPAIR_ADD_ERROR = new CodeMsg(-7000, "维修信息添加失败，请联系管理员！");
    public static CodeMsg REPAIR_EDIT_ERROR = new CodeMsg(-7001, "维修信息编辑失败，请联系管理员！");
    public static CodeMsg REPAIR_DELETE_ERROR = new CodeMsg(-7002, "维修信息删除失败，请联系管理员！");

    //投诉管理类错误码
    public static CodeMsg COMPLAINT_ADD_ERROR = new CodeMsg(-8000, "投诉信息添加失败，请联系管理员！");
    public static CodeMsg COMPLAINT_EDIT_ERROR = new CodeMsg(-8001, "投诉信息编辑失败，请联系管理员！");
    public static CodeMsg COMPLAINT_DELETE_ERROR = new CodeMsg(-8002, "投诉信息删除失败，请联系管理员！");
}
