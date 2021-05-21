package com.risefalcon.zasgateway.security_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * 属于微服务的角色
 * microservice  1 : n role
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    private String id;
    private String msId;
    private String name;

    public Role(String msId, String name) {
        this.id = UUID.randomUUID().toString();
        this.msId = msId;
        this.name = name;
    }
}
