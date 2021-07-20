package com.risefalcon.zasgateway.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.risefalcon.zasgateway.mapper.SignupMapper;
import com.risefalcon.zasgateway.security_model.Signup;
import org.springframework.stereotype.Service;

@Service
public class SignupServiceImpl extends ServiceImpl<SignupMapper, Signup>
        implements SignupService {
}
