package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.risefalcon.zasgateway.security_model.Authority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface AuthorityController extends BaseController<Authority> {
    @GetMapping("/by/{msId}")
    JSONObject getByMsId(@PathVariable String msId);
}
