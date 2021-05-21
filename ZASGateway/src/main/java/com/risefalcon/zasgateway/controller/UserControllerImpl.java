package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.risefalcon.zasgateway.security_model.User;
import com.risefalcon.zasgateway.util.Constant;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserControllerImpl implements UserController {

    @Override
    public JSONObject ins(User user) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(Constant.RESULT_KEY, Constant.NO_SERVICE);
        return jsonObject;
    }

    @Override
    public String upd(User user) {
        return Constant.NO_SERVICE;
    }

    @Override
    public String del(String id) {
        return Constant.NO_SERVICE;
    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
