package com.dataart.dancestudio.utils;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextFacadeImpl implements SecurityContextFacade {

    @Override
    public SecurityContext getContext() {
        return SecurityContextHolder.getContext();
    }

}
