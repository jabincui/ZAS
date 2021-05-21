package com.risefalcon.zasgateway.security;

import com.risefalcon.zasgateway.util.Constant;
import com.risefalcon.zasgateway.security_model.UserRole;
import com.risefalcon.zasgateway.security_model.User;
import com.risefalcon.zasgateway.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    RedisService redisService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("loadUserByUsername:" + s);
        if (s == null || "".equals(s)) {
            throw new RuntimeException("用户不能为空");
        }

        // 调用方法查询用户
        User user = redisService.get(Constant.USER, s, User.class);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // 筛选用户所有的角色
        List<UserRole> userRoles = redisService.getValues(Constant.USER_ROLE, UserRole.class);
        log.info("user roles: " + userRoles);
        userRoles.removeIf(ur -> !ur.getUserId().equals(s));
        log.info("user roles: " + userRoles);

        for (UserRole userRole : userRoles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRoleId()));
        }
        return new org.springframework.security.core.userdetails.User
                (user.getUsername(),"{noop}"+user.getPassword(),authorities);

    }
}
