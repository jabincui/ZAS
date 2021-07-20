package com.risefalcon.zasgateway.security_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * 下游微服务
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Microservice {
    public static final String ID = "id";
    public static final String NAME = "name";

    private String id;
    private String name;

    public Microservice(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }
}
