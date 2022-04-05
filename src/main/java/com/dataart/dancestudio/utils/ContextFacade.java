package com.dataart.dancestudio.utils;

import org.springframework.security.core.context.SecurityContext;

public interface ContextFacade {

    SecurityContext getContext();

}
