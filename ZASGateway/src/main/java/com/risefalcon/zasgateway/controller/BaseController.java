package com.risefalcon.zasgateway.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

public interface BaseController<T> {
    @PostMapping
    JSONObject ins(@RequestBody T t);
    @PostMapping("/update")
    String upd(@RequestBody T t);
    @PostMapping("/delete")
    String del(@RequestBody String id);
    @GetMapping("/all")
    List<T> getAll();
//    @GetMapping("/{id}")
//    T getById(@RequestParam String id);
}
