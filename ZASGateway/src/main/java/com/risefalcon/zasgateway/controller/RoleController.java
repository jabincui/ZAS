package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.risefalcon.zasgateway.security_model.Role;
import org.springframework.web.bind.annotation.*;

public interface RoleController extends BaseController<Role> {

    @GetMapping("/by/{msId}")
    JSONObject getByMsId(@PathVariable String msId);

//    @GetMapping("/by/id/{id}")
//    JSONObject getById(@PathVariable String id);

}
