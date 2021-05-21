package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSON;
import com.risefalcon.zasgateway.util.Constant;
import com.risefalcon.zasgateway.security_model.Signup;
import com.risefalcon.zasgateway.security_model.UserRole;
import com.risefalcon.zasgateway.security_model.User;
import com.risefalcon.zasgateway.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private RedisService redisService;

    /**
     * 注册
     * @param username 用户名（唯一，不可修改）
     * @param password 密码（仅支持明文）
     * @param signupId 用于获取角色池
     * @return
     *      INVALID：任何参数为空或是空串
     *      EXIST：username已存在
     *      NOT_EXIST：signupId不存在
     */
    @PostMapping("/signup")
    public String signup(@RequestHeader String username,
                         @RequestHeader String password,
                         @RequestHeader String signupId) {
        if (username == null || username.equals("") || password == null
                || password.equals("") || signupId == null || signupId.equals("")) {
            return Constant.INVALID;
        }

        if (redisService.exist(Constant.USER, username)) {
            log.info("Username collapsed.");
            return Constant.EXIST;
        }

        User user = new User(username, password);
        redisService.put(Constant.USER, user.getUsername(), JSON.toJSONString(user));

        if (!redisService.exist(Constant.SIGNUP, signupId)) {
            return Constant.NOT_EXIST;
        }
        for (String roleId: redisService
                .get(Constant.SIGNUP, signupId, Signup.class)
                .getRolesId()) {
            log.info("roles id: " + roleId);
            if (!redisService.exist(Constant.ROLE, roleId)) {
                continue;
            }

            UserRole userRole = new UserRole(user.getUsername(), roleId);
            redisService.put(Constant.USER_ROLE, userRole.getId(),
                    JSON.toJSONString(userRole)
            );
        }

        return Constant.PASS;
    }




//    /**
//     *
//     * @return 空表返回 EMPTY
//     */
//    @GetMapping("/all_registered_roles")
//    public String getRegisteredRoles() {
//        List<String> registeredRoles =
//                redisService.getValues("registered_role", String.class);
//        if (registeredRoles == null) {
//            log.info("null");
//            return Constant.EMPTY;
//        }
//        if (registeredRoles.size() == 0) {
//            log.info("size 0");
//            return Constant.EMPTY;
//        }
//        return JSON.toJSONString(registeredRoles);
//    }
//
}
