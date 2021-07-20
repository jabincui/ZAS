package com.risefalcon.zasgateway.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.risefalcon.zasgateway.mapper.UserRoleMapper;
import com.risefalcon.zasgateway.security_model.UserRole;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
        implements UserRoleService {
}
