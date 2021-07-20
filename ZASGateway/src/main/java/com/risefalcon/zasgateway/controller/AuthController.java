package com.risefalcon.zasgateway.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.risefalcon.zasgateway.security_model.*;
import com.risefalcon.zasgateway.service.*;
import com.risefalcon.zasgateway.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private SignupServiceImpl signupService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;

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

        if ( null != userService.getOne(new QueryWrapper<User>().eq(User.USERNAME, username)) ) {
            log.info("Username collapsed.");
            return Constant.EXIST;
        }
        if ( null == signupService.getOne(new QueryWrapper<Signup>().eq(Signup.ID, signupId)) ) {
            return Constant.NOT_EXIST;
        }

        User user = new User(username, password);
        userService.save(user);
        for (String roleId: signupService
                .getOne(new QueryWrapper<Signup>().eq(Signup.ID, signupId))
                .getRolesId()) {
            log.info("roles id: " + roleId);
            // 验证role id合法性
            if ( null == roleService.getOne(new QueryWrapper<Role>().eq(Role.ID, roleId)) ) {
                continue;
            }
            UserRole userRole = new UserRole(user.getUsername(), roleId);
            userRoleService.save(userRole);
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
