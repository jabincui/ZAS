package com.risefalcon.zasgateway.config;

import com.risefalcon.zasgateway.service.MicroserviceServiceImpl;
import com.risefalcon.zasgateway.util.RouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class ZuulConfig {
    @Autowired
    ZuulProperties zuulProperties;
    @Autowired
    ServerProperties server;
    @Lazy
    @Autowired
    MicroserviceServiceImpl microserviceService;

    @Bean
    public RouteLocator routeLocator() {
        return new RouteLocator(this.server.getServlet().getContextPath(),
                this.zuulProperties, this.microserviceService);
    }
}
