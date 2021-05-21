package com.risefalcon.zasgateway.security_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authority {
    private String id;
    private String msId;
    private String urlId;
    private String roleId;

    public Authority(String msId, String urlId, String roleId) {
        this.id = UUID.randomUUID().toString();
        this.msId = msId;
        this.urlId = urlId;
        this.roleId = roleId;
    }
}
