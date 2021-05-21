package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.risefalcon.zasgateway.security_model.UserRole;
import com.risefalcon.zasgateway.service.RedisService;
import com.risefalcon.zasgateway.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/user_role")
public class UserRoleControllerImpl implements UserRoleController {

    @Autowired
    private RedisService redisService;

    @Override
    public JSONObject ins(UserRole userRole) {
        JSONObject jsonObject = new JSONObject();
        if (userRole.getUserId() == null || userRole.getUserId().equals("")
                || userRole.getRoleId() == null || userRole.getRoleId().equals("")) {
            jsonObject.put(Constant.RESULT_KEY, Constant.INVALID);
            return jsonObject;
        }
        if ((!redisService.exist(Constant.USER, userRole.getUserId()))
                || (!redisService.exist(Constant.ROLE, userRole.getRoleId()))) {
            jsonObject.put(Constant.RESULT_KEY, Constant.NOT_EXIST);
            return jsonObject;
        }
        for (UserRole ur: redisService.getValues(Constant.USER_ROLE, UserRole.class)) {
            if (ur.getUserId().equals(userRole.getUserId())
                    && ur.getRoleId().equals(userRole.getRoleId())) {
                jsonObject.put(Constant.RESULT_KEY, Constant.EXIST);
                return jsonObject;
            }
        }
        UserRole ur = new UserRole(userRole.getUserId(), userRole.getRoleId());
        redisService.put(Constant.USER_ROLE, ur.getId(),
                JSONObject.toJSONString(ur));
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
        if (!redisService.exist(Constant.USER_ROLE, id)) {
            return Constant.NOT_EXIST;
        }
        redisService.delete(Constant.USER_ROLE, id);
        return Constant.PASS;
    }

    @Override
    public List<UserRole> getAll() {
        return redisService.getValues(Constant.USER_ROLE, UserRole.class);
    }
}
