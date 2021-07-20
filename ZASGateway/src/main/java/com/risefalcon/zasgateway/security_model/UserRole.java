package com.risefalcon.zasgateway.security_model;

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
    public static final String ID = "id";
    public static final String USERID = "user_id";
    public static final String ROLEID = "role_id";

    private String id;
    private String userId;
    private String roleId;

    public UserRole(String userId, String roleId) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.roleId = roleId;
    }
}
