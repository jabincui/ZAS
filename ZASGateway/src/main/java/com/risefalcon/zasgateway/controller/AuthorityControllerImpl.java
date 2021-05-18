package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.risefalcon.zasgateway.config.Constant;
import com.risefalcon.zasgateway.model.Authority;
import com.risefalcon.zasgateway.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/authority")
public class AuthorityControllerImpl implements AuthorityController {

    @Autowired
    private RedisService redisService;

    @Override
    public JSONObject ins(Authority authority) {
        JSONObject jsonObject = new JSONObject();
        if (authority.getUrlId() == null || authority.getUrlId().equals("")
                || authority.getRoleId() == null
                || authority.getRoleId().equals("")) {
            jsonObject.put(Constant.RESULT_KEY, Constant.INVALID);
            return jsonObject;
        }
        if (redisService.exist(Constant.AUTHORITY,
                authority.getUrlId() + authority.getRoleId())) {
            jsonObject.put(Constant.RESULT_KEY, Constant.EXIST);
            return jsonObject;
        }
        Authority a = new Authority();
        redisService.put(Constant.AUTHORITY,
                authority.getId(),
                JSON.toJSONString(authority));
        jsonObject.put(Constant.RESULT_KEY, Constant.PASS);
        jsonObject.put(Constant.OBJ, a);
        return jsonObject;
    }

    @Override
    public String upd(Authority authority) {
        return Constant.NO_SERVICE;
    }

    @Override
    public String del(String id) {
        if (id == null || id.equals("")) {
            return Constant.INVALID;
        }
        if (!redisService.exist(Constant.AUTHORITY, id)) {
            return Constant.NOT_EXIST;
        }
        redisService.delete(Constant.AUTHORITY, id);
        return Constant.PASS;
    }

    @Override
    public List<Authority> getAll() {
        return redisService.getValues(Constant.AUTHORITY, Authority.class);
    }

}
