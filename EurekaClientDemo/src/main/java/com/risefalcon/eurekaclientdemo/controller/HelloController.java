package com.risefalcon.eurekaclientdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        return "Hello, ZAS!";
    }
    @PostMapping("/helloo")
    public String helloO() {
        return "Hello, ZAS!";
    }
}
