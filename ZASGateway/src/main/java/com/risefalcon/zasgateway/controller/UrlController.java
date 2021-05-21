package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.risefalcon.zasgateway.security_model.URL;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface UrlController extends BaseController<URL> {

    @GetMapping("/by/{msId}")
    JSONObject getByMsId(@PathVariable String msId);
}
