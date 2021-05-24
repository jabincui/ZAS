package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.risefalcon.zasgateway.security_model.Microservice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface MicroserviceController extends BaseController<Microservice> {

    @GetMapping("/by/id/{id}")
    JSONObject getById(@PathVariable String id);
}
