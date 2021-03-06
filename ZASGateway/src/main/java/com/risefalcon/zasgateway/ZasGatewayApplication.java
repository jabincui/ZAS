package com.risefalcon.zasgateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * 技术栈 Zuul + Spring Security + Eureka + Redis
 */
@Slf4j
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class ZasGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZasGatewayApplication.class, args);
    }

}
