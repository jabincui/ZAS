package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.risefalcon.zasgateway.util.Constant;
import com.risefalcon.zasgateway.security_model.Authority;
import com.risefalcon.zasgateway.security_model.URL;
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
            jsonObject.put(Constant.RESULT_KEY, Constant.INVALID);
            return jsonObject;
        }

        if (!redisService.exist(Constant.MICROSERVICE, url.getMsId())) {
            jsonObject.put(Constant.MICROSERVICE, Constant.NOT_EXIST);
            return jsonObject;
        }

        for (URL u: redisService.getValues(Constant.URL, URL.class)) {
            if (u.getMsId().equals(url.getMsId())
                    && u.getPath().equals(url.getPath())) {
                jsonObject.put(Constant.RESULT_KEY, Constant.EXIST);
                return jsonObject;
            }
        }
        URL u = new URL(url.getMsId(), url.getPath());
        redisService.put(Constant.URL, u.getId(), JSON.toJSONString(u));
        jsonObject.put(Constant.RESULT_KEY, Constant.PASS);
        jsonObject.put(Constant.OBJ, u);
        return jsonObject;
    }

    @Override
    public String upd(URL url) {
        if (url.getId() == null || url.getId().equals("")) {
            return Constant.INVALID;
        }
        if (!redisService.exist(Constant.URL, url.getId())) {
            return Constant.NOT_EXIST;
        }
        for (URL u: redisService.getValues(Constant.URL, URL.class)) {
            if (u.getMsId().equals(url.getMsId())
                    && u.getPath().equals(url.getPath())) {
                return Constant.EXIST;
            }
        }
        url.setMsId(redisService.get(Constant.URL, url.getId(), URL.class).getMsId());
        redisService.put(Constant.URL, url.getId(), JSON.toJSONString(url));
        return Constant.PASS;
    }

    @Override
    public String del(String id) {
        if (id == null || id.equals("")) {
            return Constant.INVALID;
        }
        if (!redisService.exist(Constant.URL, id)) {
            return Constant.NOT_EXIST;
        }

        // 删除有关权限项
        for (Authority authority: redisService.getValues(Constant.AUTHORITY, Authority.class)) {
            if (authority.getUrlId().equals(id)) {
                redisService.delete(Constant.AUTHORITY, authority.getId());
            }
        }

        redisService.delete(Constant.URL, id);
        return Constant.PASS;
    }

    @Override
    public List<URL> getAll() {
        return redisService.getValues(Constant.URL, URL.class);
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
        List<URL> urls = redisService.getValues(Constant.URL, URL.class);
        urls.removeIf(u -> !u.getMsId().equals(msId));
        jsonObject.put(Constant.RESULT_KEY, Constant.PASS);
        jsonObject.put(Constant.OBJ, urls);
        return jsonObject;
    }
}
