package com.risefalcon.zasgateway.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.risefalcon.zasgateway.mapper.URLMapper;
import com.risefalcon.zasgateway.security_model.URL;
import org.springframework.stereotype.Service;

@Service
public class URLServiceImpl extends ServiceImpl<URLMapper, URL>
        implements URLService {
}
