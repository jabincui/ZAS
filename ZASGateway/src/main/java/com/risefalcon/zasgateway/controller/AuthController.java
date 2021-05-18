package com.risefalcon.zasgateway.controller;


import com.alibaba.fastjson.JSON;
import com.risefalcon.zasgateway.config.Constant;
import com.risefalcon.zasgateway.model.UserRole;
import com.risefalcon.zasgateway.model.User;
import com.risefalcon.zasgateway.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

// TODO 自定义注册url
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    RedisService redisService;

//    @PostMapping("/user")
//    public String signup(@RequestBody User user) {
//        if (user == null || user.getUsername() ==null || user.getPassword() == null)
//            return "INVALID";
//        QueryWrapper<User> uqw = new QueryWrapper<>();
//        uqw.eq("username", user.getUsername());
//        if (userService.getOne(uqw) != null) return "EXIST";
//        userService.save(user);
//        roleService.save(new Role(user.getUsername(), "USER"));
//        return "PASS";
//    }

    /**
     * 注册
     * @param username 用户名
     * @param password 密码（仅支持明文）
     * @param roles 角色，多角色用空格分隔，角色对应一种或几种权限，在角色-权限表中查
     * @return 处理状态
     */
    @PostMapping("/signup")
    public String signup(@RequestHeader String username,
                         @RequestHeader String password,
                         @RequestHeader("roles") String roles) {
        if (redisService.exist("user", username)) {
            log.info("Username collapsed.");
            return Constant.EXIST;
        }
        redisService.put("user", username,
                JSON.toJSONString(new User(username, password)));
        String[]  roleArr = roles.split(" ");
        for (String role: roleArr) {
            redisService.put("role", username + new Date().getTime(),
                    JSON.toJSONString(new UserRole(username, role))
            );
        }


        log.info(redisService.getValues("role", UserRole.class).toString());
        return Constant.PASS;
    }


    /**
     *
     * @return 空表返回 EMPTY
     */
    @GetMapping("/all_registered_roles")
    public String getRegisteredRoles() {
        List<String> registeredRoles =
                redisService.getValues("registered_role", String.class);
        if (registeredRoles == null) {
            log.info("null");
            return Constant.EMPTY;
        }
        if (registeredRoles.size() == 0) {
            log.info("size 0");
            return Constant.EMPTY;
        }
        return JSON.toJSONString(registeredRoles);
    }
    
}
