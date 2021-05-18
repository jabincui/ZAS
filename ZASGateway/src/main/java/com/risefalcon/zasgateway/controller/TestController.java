package com.risefalcon.zasgateway.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {




    @GetMapping("/hello")
    public String testSecurity() {
        return "SUCCESS";
    }


    @Autowired
    ConfigService configService;

    @GetMapping("/print_config")
    public void printConfig() {

    }
}
