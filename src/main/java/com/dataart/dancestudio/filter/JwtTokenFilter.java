package com.dataart.dancestudio.filter;

import com.dataart.dancestudio.provider.JwtTokenProvider;
import com.dataart.dancestudio.service.JwtTokenService;
import com.dataart.dancestudio.utils.SecurityContextFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final static String BEARER = "Bearer ";
    private final SecurityContextFacade securityContextFacade;
    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(final HttpServletRequest request, @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || authHeader.isBlank() || !authHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }
        final String jwtToken = authHeader.substring(BEARER.length());
        if (jwtToken.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            log.warn("Access Token invalid in Bearer Header! Can't get access to resource.");
            return;
        }
        if (!jwtTokenProvider.validateAccessToken(jwtToken) || !jwtTokenService.existsByToken(jwtToken)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            log.warn("Access Token invalid! Can't get access to resource.");
            return;
        }
        final String email = jwtTokenProvider.getEmail(jwtToken);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        final UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());
        final SecurityContext securityContext = securityContextFacade.getContext();
        if (securityContext.getAuthentication() == null) {
            securityContext.setAuthentication(authToken);
        }
        log.info("Access Token for email = {} valid.", email);
        filterChain.doFilter(request, response);
    }

}
