package com.yjq.programmer.controller;

import com.yjq.programmer.dto.PageDTO;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.dto.UserDTO;
import com.yjq.programmer.service.IUserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author 杨杨吖
 * @QQ 823208782
 * @WX yjqi12345678
 * @create 2024-04-14 19:22
 */
@RestController("UserController")
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    /**
     * 分页获取用户数据
     * @param pageDTO
     * @return
     */
    @PostMapping("/list")
    public ResponseDTO<PageDTO<UserDTO>> getUserList(@RequestBody PageDTO<UserDTO> pageDTO){
        return userService.getUserList(pageDTO);
    }

    /**
     * 用户登录操作
     * @param userDTO
     * @return
     */
    @PostMapping("/login")
    public ResponseDTO<UserDTO> login(@RequestBody UserDTO userDTO){
        return userService.login(userDTO);
    }

    /**
     * 检查用户是否登录
     * @param userDTO
     * @return
     */
    @PostMapping("/check_login")
    public ResponseDTO<UserDTO> checkLogin(@RequestBody UserDTO userDTO){
        return userService.checkLogin(userDTO);
    }


    /**
     * 用户退出登录
     * @return
     */
    @PostMapping("/logout")
    public ResponseDTO<Boolean> logout(@RequestBody UserDTO userDTO){
        return userService.logout(userDTO);
    }

    /**
     * 删除用户信息
     * @param userDTO
     * @return
     */
    @PostMapping("/delete")
    public ResponseDTO<Boolean> deleteUser(@RequestBody UserDTO userDTO){
        return userService.deleteUser(userDTO);
    }

    /**
     * 保存用户信息
     * @param userDTO
     * @return
     */
    @PostMapping("/save")
    public ResponseDTO<Boolean> saveUser(@RequestBody UserDTO userDTO){
        return userService.saveUser(userDTO);
    }


    /**
     * 根据条件查询所有用户信息
     * @param userDTO
     * @return
     */
    @PostMapping("/all")
    public ResponseDTO<List<UserDTO>> getAllUserList(@RequestBody UserDTO userDTO) {
        return userService.getAllUserList(userDTO);
    }


    /**
     * 系统首页总数统计
     * @param userDTO
     * @return
     */
    @PostMapping("/statistic")
    public ResponseDTO<Map<String, Object>> getTotalStatistic(@RequestBody UserDTO userDTO) {
        return userService.getTotalStatistic(userDTO);
    }

}
