package com.dataart.dancestudio.utils;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ContextFacadeImpl implements ContextFacade {

    @Override
    public SecurityContext getContext() {
        return SecurityContextHolder.getContext();
    }

}
