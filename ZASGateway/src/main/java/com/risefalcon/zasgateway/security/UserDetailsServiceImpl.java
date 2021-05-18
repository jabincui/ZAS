package com.risefalcon.zasgateway.security;


import com.risefalcon.zasgateway.model.UserRole;
import com.risefalcon.zasgateway.model.User;
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

        User user = redisService.getObj("USER" + s, User.class);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        List<UserRole> roles = redisService.getValues("ROLE", UserRole.class);
        for (UserRole role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleId()));
        }
        return new org.springframework.security.core.userdetails.User
                (user.getUsername(),"{noop}"+user.getPassword(),authorities);

    }
}
