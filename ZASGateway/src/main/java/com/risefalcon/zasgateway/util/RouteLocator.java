package com.risefalcon.zasgateway.util;

import com.risefalcon.zasgateway.security_model.Microservice;
import com.risefalcon.zasgateway.service.RedisService;
import com.risefalcon.zasgateway.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.util.StringUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class RouteLocator extends SimpleRouteLocator
        implements RefreshableRouteLocator {

    private RedisService redisService;
    private ZuulProperties properties;

    public RouteLocator(String servletPath, ZuulProperties properties, RedisService redisService) {
        super(servletPath, properties);
        this.properties = properties;
        this.redisService = redisService;
        log.info("servletPath:{}", servletPath);

    }

    @Override
    public void refresh() {
        doRefresh();
    }

    @Override
    protected Map<String, ZuulProperties.ZuulRoute> locateRoutes() {
        LinkedHashMap<String, ZuulProperties.ZuulRoute> routesMap
                = new LinkedHashMap<>();
        //从application.properties中加载路由信息
        routesMap.putAll(super.locateRoutes());
        //从db中加载路由信息
        routesMap.putAll(locateRoutesFromDB());

        //优化一下配置
        LinkedHashMap<String, ZuulProperties.ZuulRoute> values = new LinkedHashMap<>();
        for (Map.Entry<String, ZuulProperties.ZuulRoute> entry : routesMap.entrySet()) {
            String path = entry.getKey();
            // Prepend with slash if not already present.
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (StringUtils.hasText(this.properties.getPrefix())) {
                path = this.properties.getPrefix() + path;
                if (!path.startsWith("/")) {
                    path = "/" + path;
                }
            }
            values.put(path, entry.getValue());
        }
        return values;
    }

    private Map<String, ZuulProperties.ZuulRoute> locateRoutesFromDB() {
        Map<String, ZuulProperties.ZuulRoute> routes = new LinkedHashMap<>();

        for (Microservice microservice: redisService.getValues(Constant.MICROSERVICE, Microservice.class)) {
            ZuulProperties.ZuulRoute zuulRoute = new ZuulProperties.ZuulRoute(
                    UUID.randomUUID().toString(), "/" + microservice.getName() + "/**",
                    microservice.getName(), null, true, true, null
            );
            routes.put(zuulRoute.getPath(), zuulRoute);
        }
        return routes;
    }

}
