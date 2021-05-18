package com.risefalcon.zasgateway.security;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;

import java.util.Collection;

public class RoleBasedVoter implements AccessDecisionVoter<Object> {

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }

    @Override
    public int vote(Authentication authentication, Object o,
                    Collection<ConfigAttribute> collection) {
        return 0;
    }
}
