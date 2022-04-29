package com.dataart.dancestudio.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dataart.dancestudio.utils.JwtTokenUtil;
import com.dataart.dancestudio.utils.SecurityContextFacade;
import org.springframework.beans.factory.annotation.Autowired;
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

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final SecurityContextFacade securityContextFacade;
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtUtil;

    @Autowired
    public JwtTokenFilter(final SecurityContextFacade securityContextFacade, final UserDetailsService userDetailsService,
                          final JwtTokenUtil jwtUtil) {
        this.securityContextFacade = securityContextFacade;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, @NonNull final HttpServletResponse response,
                                    @NonNull final FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {
            final String jwt = authHeader.substring(7);
            if (jwt.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token in Bearer Header");
            } else {
                try {
                    final String email = jwtUtil.validateTokenAndRetrieveSubject(jwt);
                    final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    final UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());
                    final SecurityContext securityContext = securityContextFacade.getContext();
                    if (securityContext.getAuthentication() == null) {
                        securityContext.setAuthentication(authToken);
                    }
                } catch (final JWTVerificationException exc) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
