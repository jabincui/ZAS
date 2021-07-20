package com.risefalcon.zasgateway.security;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.risefalcon.zasgateway.service.UserRoleServiceImpl;
import com.risefalcon.zasgateway.service.UserServiceImpl;
import com.risefalcon.zasgateway.util.Constant;
import com.risefalcon.zasgateway.security_model.UserRole;
import com.risefalcon.zasgateway.security_model.User;
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
    private UserServiceImpl userService;
    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        log.info("loadUserByUsername:" + s);
        if (s == null || "".equals(s)) {
            throw new RuntimeException("用户不能为空");
        }

        // 调用方法查询用户
        User user = userService.getById(s);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // 筛选用户所有的角色
        List<UserRole> userRoles = userRoleService
                .list(new QueryWrapper<UserRole>().eq(UserRole.USERID, s));

        for (UserRole userRole : userRoles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRole.getRoleId()));
        }
        return new org.springframework.security.core.userdetails.User
                (user.getUsername(),"{noop}"+user.getPassword(),authorities);

    }
}
