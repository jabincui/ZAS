package com.risefalcon.zasgateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * 用户-角色
 * 登录需要USER身份，业务中需要判定身份时，用账号所在的表判断
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {
    private String id;
    private String userId;
    private String roleId;

    public UserRole(String username, String name) {
        this.id = UUID.randomUUID().toString();
        this.userId = username;
        this.roleId = name;
    }
}
