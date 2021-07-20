package com.risefalcon.zasgateway.security_model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全局用户，不区分服务
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    @TableId
    private String username;
    private String password;
}
