package com.risefalcon.zasgateway.model;


import lombok.Data;


/**
 * 登录需要USER身份，业务中需要判定身份时，用账号所在的表判断
 */

@Data
public class Role {
    private Integer id;
    private String username;
    private String name;

    public Role(String username, String name) {
        this.username = username;
        this.name = name;
    }
}
