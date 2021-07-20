package com.risefalcon.zasgateway.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.risefalcon.zasgateway.mapper.MicroserviceMapper;
import com.risefalcon.zasgateway.security_model.Microservice;
import org.springframework.stereotype.Service;

@Service
public class MicroserviceServiceImpl
        extends ServiceImpl<MicroserviceMapper, Microservice>
        implements MicroserviceService {
}
