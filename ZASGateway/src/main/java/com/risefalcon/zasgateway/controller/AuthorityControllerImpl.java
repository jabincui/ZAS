package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.risefalcon.zasgateway.security_model.URL;
import com.risefalcon.zasgateway.util.Constant;
import com.risefalcon.zasgateway.security_model.Authority;
import com.risefalcon.zasgateway.service.RedisService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
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
        if (authority.getUrlId() == null
                || authority.getUrlId().equals("")
                || authority.getRoleId() == null
                || authority.getRoleId().equals("")
                || authority.getMsId() == null
                || authority.getMsId().equals("")) {
            jsonObject.put(Constant.RESULT_KEY, Constant.INVALID);
            return jsonObject;
        }

        if ((!redisService.exist(Constant.MICROSERVICE, authority.getMsId()))
                || (!redisService.exist(Constant.ROLE, authority.getRoleId()))
                || (!redisService.exist(Constant.URL, authority.getUrlId()))) {
            jsonObject.put(Constant.RESULT_KEY, Constant.NOT_EXIST);
        }

        for (Authority a: redisService.getValues(Constant.AUTHORITY, Authority.class)) {
            if (a.getMsId().equals(authority.getMsId())
                    && a.getUrlId().equals(authority.getUrlId())
                    && a.getRoleId().equals(authority.getRoleId())) {
                jsonObject.put(Constant.RESULT_KEY, Constant.EXIST);
                return jsonObject;
            }
        }


        Authority a = new Authority(authority.getMsId(), authority.getUrlId(), authority.getRoleId());
        redisService.put(Constant.AUTHORITY, a.getId(), JSON.toJSONString(a));
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


    @Override
    public JSONObject getByMsId(String msId) {
        JSONObject jsonObject = new JSONObject();
        if (msId == null || msId.equals("")) {
            jsonObject.put(Constant.RESULT_KEY, Constant.INVALID);
        }
        if (!redisService.exist(Constant.MICROSERVICE, msId)) {
            jsonObject.put(Constant.RESULT_KEY, Constant.NOT_EXIST);
            return jsonObject;
        }
        List<Authority> auths = redisService.getValues(Constant.AUTHORITY, Authority.class);
        auths.removeIf(auth -> !auth.getMsId().equals(msId));
        jsonObject.put(Constant.RESULT_KEY, Constant.PASS);
        jsonObject.put(Constant.OBJ, auths);
        return jsonObject;
    }
}
