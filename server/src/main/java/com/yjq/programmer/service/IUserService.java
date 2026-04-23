package com.yjq.programmer.service;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;

import java.util.List;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-04-14 21:48
 */
public interface IUserService {

    // 分页获取用户数据
    ResponseDTO<PageDTO<UserDTO>> getUserList(PageDTO<UserDTO> pageDTO);

    // 保存用户信息
    ResponseDTO<Boolean> saveUser(UserDTO userDTO);

    // 删除用户信息
    ResponseDTO<Boolean> deleteUser(UserDTO userDTO);

    // 用户登录操作
    ResponseDTO<UserDTO> login(UserDTO userDTO);

    // 用户退出登录
    ResponseDTO<Boolean> logout(UserDTO userDTO);

    // 检查用户是否登录
    ResponseDTO<UserDTO> checkLogin(UserDTO userDTO);

    // 根据条件查询所有用户信息
    ResponseDTO<List<UserDTO>> getAllUserList(UserDTO userDTO);

    // 系统首页总数统计
    ResponseDTO<Map<String, Object>> getTotalStatistic(UserDTO userDTO);
}
