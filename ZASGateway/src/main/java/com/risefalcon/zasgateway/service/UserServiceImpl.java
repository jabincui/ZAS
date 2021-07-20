package com.risefalcon.zasgateway.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.risefalcon.zasgateway.mapper.UserMapper;
import com.risefalcon.zasgateway.security_model.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
}
