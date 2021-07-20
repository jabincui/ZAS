package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.risefalcon.zasgateway.security_model.UserRole;
import com.risefalcon.zasgateway.service.RoleServiceImpl;
import com.risefalcon.zasgateway.service.UserRoleServiceImpl;
import com.risefalcon.zasgateway.service.UserServiceImpl;
import com.risefalcon.zasgateway.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/user_role")
public class UserRoleControllerImpl implements UserRoleController {

    @Autowired
    private UserRoleServiceImpl userRoleService;
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private RoleServiceImpl roleService;

    @Override
    public JSONObject ins(UserRole userRole) {
        JSONObject jsonObject = new JSONObject();
        if (userRole.getUserId() == null || userRole.getUserId().equals("")
                || userRole.getRoleId() == null || userRole.getRoleId().equals("")) {
            jsonObject.put(Constant.RESULT_KEY, Constant.INVALID);
            return jsonObject;
        }
        if ( null == userService.getById(userRole.getUserId())
                || null == roleService.getById(userRole.getRoleId()) ) {
            jsonObject.put(Constant.RESULT_KEY, Constant.NOT_EXIST);
            return jsonObject;
        }
        if ( null != userRoleService.getOne(new QueryWrapper<UserRole>()
                .eq(UserRole.USERID, userRole.getUserId())
                .eq(UserRole.ROLEID, userRole.getRoleId())) ) {
            jsonObject.put(Constant.RESULT_KEY, Constant.EXIST);
            return jsonObject;
        }
        UserRole ur = new UserRole(userRole.getUserId(), userRole.getRoleId());
        userRoleService.save(ur);
        jsonObject.put(Constant.RESULT_KEY, Constant.PASS);
        jsonObject.put(Constant.OBJ, ur);
        return jsonObject;
    }

    @Override
    public String upd(UserRole userRole) {
        return Constant.NO_SERVICE;
    }

    @Override
    public String del(String id) {
        if (id == null || id.equals("")) {
            return Constant.INVALID;
        }
        if (null == userRoleService.getById(id)) {
            return Constant.NOT_EXIST;
        }
        userRoleService.removeById(id);
        return Constant.PASS;
    }

    @Override
    public List<UserRole> getAll() {
        return userRoleService.list();
    }
}
