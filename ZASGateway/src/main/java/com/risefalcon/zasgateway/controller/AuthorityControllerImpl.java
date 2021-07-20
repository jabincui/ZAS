package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.risefalcon.zasgateway.security_model.Microservice;
import com.risefalcon.zasgateway.security_model.URL;
import com.risefalcon.zasgateway.service.*;
import com.risefalcon.zasgateway.util.Constant;
import com.risefalcon.zasgateway.security_model.Authority;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/authority")
public class AuthorityControllerImpl implements AuthorityController {

    @Autowired
    private AuthorityServiceImpl authorityService;
    @Autowired
    private MicroserviceServiceImpl microserviceService;
    @Autowired
    private RoleServiceImpl roleService;
    @Autowired
    private URLServiceImpl urlService;

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

        if ( (null == microserviceService.getById(authority.getMsId()))
                || (null == roleService.getById(authority.getRoleId()))
                || (null == urlService.getById(authority.getUrlId())) ) {
            jsonObject.put(Constant.RESULT_KEY, Constant.NOT_EXIST);
        }

        for (Authority a: authorityService.list()) {
            if (a.getMsId().equals(authority.getMsId())
                    && a.getUrlId().equals(authority.getUrlId())
                    && a.getRoleId().equals(authority.getRoleId())) {
                jsonObject.put(Constant.RESULT_KEY, Constant.EXIST);
                return jsonObject;
            }
        }

        Authority a = new Authority(authority.getMsId(),
                authority.getUrlId(), authority.getRoleId());
        authorityService.save(a);
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
        if (null == authorityService.getById(id)) {
            return Constant.NOT_EXIST;
        }
        authorityService.removeById(id);
        return Constant.PASS;
    }

    @Override
    public List<Authority> getAll() {
        return authorityService.list();
    }


    @Override
    public JSONObject getByMsId(String msId) {
        JSONObject jsonObject = new JSONObject();
        if (msId == null || msId.equals("")) {
            jsonObject.put(Constant.RESULT_KEY, Constant.INVALID);
        }
        if (null == microserviceService.getById(msId)) {
            jsonObject.put(Constant.RESULT_KEY, Constant.NOT_EXIST);
            return jsonObject;
        }
        List<Authority> auths = authorityService
                .list(new QueryWrapper<Authority>().eq(Microservice.ID, msId));
        jsonObject.put(Constant.RESULT_KEY, Constant.PASS);
        jsonObject.put(Constant.OBJ, auths);
        return jsonObject;
    }
}
