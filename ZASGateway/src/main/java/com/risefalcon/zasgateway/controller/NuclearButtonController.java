package com.risefalcon.zasgateway.controller;

import com.risefalcon.zasgateway.service.RedisService;
import com.risefalcon.zasgateway.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NuclearButtonController {


    @Autowired
    private RedisService redisService;

    @PostMapping("/boom")
    public String boom(@RequestHeader String areusure) {
        if (areusure.equals("IMSURE,lklsdianflaniodfanknfienkandifhadnfksai")) {
            redisService.deleteAll(Constant.MICROSERVICE);
            redisService.deleteAll(Constant.URL);
            redisService.deleteAll(Constant.ROLE);
            redisService.deleteAll(Constant.USER);
            redisService.deleteAll(Constant.AUTHORITY);
            redisService.deleteAll(Constant.USER_ROLE);
            return "BOOM!";
        }
        return "Who are you?";
    }
}
