package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.risefalcon.zasgateway.model.URL;
import com.risefalcon.zasgateway.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/url")
public class UrlControllerImpl implements UrlController {

    @Autowired
    private RedisService redisService;

    @Override
    public JSONObject ins(URL url) {
        JSONObject jsonObject = new JSONObject();
        if (url.getMsId() == null || url.getMsId().equals("")
                || url.getPath() == null || url.getPath().equals("")) {

        }
        return null;
    }

    @Override
    public String upd(URL url) {
        return null;
    }

    @Override
    public String del(String id) {
        return null;
    }

    @Override
    public List<URL> getAll() {
        return null;
    }
}
