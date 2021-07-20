package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.risefalcon.zasgateway.service.AuthorityServiceImpl;
import com.risefalcon.zasgateway.service.MicroserviceServiceImpl;
import com.risefalcon.zasgateway.service.URLServiceImpl;
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
    private URLServiceImpl urlService;
    @Autowired
    private MicroserviceServiceImpl microserviceService;
    @Autowired
    private AuthorityServiceImpl authorityService;

    @Override
    public JSONObject ins(URL url) {
        JSONObject jsonObject = new JSONObject();
        if (url.getMsId() == null || url.getMsId().equals("")
                || url.getPath() == null || url.getPath().equals("")) {
            jsonObject.put(Constant.RESULT_KEY, Constant.INVALID);
            return jsonObject;
        }

        if (null == microserviceService.getById(url.getMsId())) {
            jsonObject.put(Constant.MICROSERVICE, Constant.NOT_EXIST);
            return jsonObject;
        }

        if ( null != urlService.getOne(new QueryWrapper<URL>()
                .eq(URL.MSID, url.getMsId())
                .eq(URL.PATH, url.getPath())) ) {
            jsonObject.put(Constant.RESULT_KEY, Constant.EXIST);
            return jsonObject;
        }

        URL u = new URL(url.getMsId(), url.getPath());
        urlService.save(u);
        jsonObject.put(Constant.RESULT_KEY, Constant.PASS);
        jsonObject.put(Constant.OBJ, u);
        return jsonObject;
    }

    @Override
    public String upd(URL url) {
        if (url.getId() == null || url.getId().equals("")) {
            return Constant.INVALID;
        }
        URL url1 = urlService.getById(url.getId());
        if ( null == url1 ) {
            return Constant.NOT_EXIST;
        }
        if (null != urlService.getOne(new QueryWrapper<URL>()
                .eq(URL.MSID, url.getMsId())
                .eq(URL.PATH, url.getPath())) ) {
            return Constant.EXIST;
        }
        url.setMsId(url1.getMsId());
        urlService.saveOrUpdate(url);
        return Constant.PASS;
    }

    @Override
    public String del(String id) {
        if (id == null || id.equals("")) {
            return Constant.INVALID;
        }
        if (null == urlService.getById(id)) {
            return Constant.NOT_EXIST;
        }

        // 删除有关权限项
        authorityService.remove(new QueryWrapper<Authority>().eq(Authority.URLID, id));
        urlService.removeById(id);
        return Constant.PASS;
    }

    @Override
    public List<URL> getAll() {
        return urlService.list();
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
        List<URL> urls = urlService.list(new QueryWrapper<URL>().eq(URL.MSID, msId));
        jsonObject.put(Constant.RESULT_KEY, Constant.PASS);
        jsonObject.put(Constant.OBJ, urls);
        return jsonObject;
    }
}
