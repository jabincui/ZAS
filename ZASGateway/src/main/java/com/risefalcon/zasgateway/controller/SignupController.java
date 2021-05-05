package com.risefalcon.zasgateway.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO 自定义注册url
@RestController
@RequestMapping("/signup")
public class SignupController {
//    @Autowired
//    private UserServiceImpl userService;
//    @Autowired
//    private RoleServiceImpl roleService;
//
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
}
