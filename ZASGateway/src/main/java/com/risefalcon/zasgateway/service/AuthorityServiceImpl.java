package com.risefalcon.zasgateway.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.risefalcon.zasgateway.mapper.AuthorityMapper;
import com.risefalcon.zasgateway.security_model.Authority;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl extends ServiceImpl<AuthorityMapper, Authority>
        implements AuthorityService {
}
