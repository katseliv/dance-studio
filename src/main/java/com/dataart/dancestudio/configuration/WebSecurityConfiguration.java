package com.dataart.dancestudio.configuration;

import com.dataart.dancestudio.filter.JwtTokenFilter;
import com.dataart.dancestudio.mapper.UserMapper;
import com.dataart.dancestudio.repository.UserRepository;
import com.dataart.dancestudio.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenFilter jwtTokenFilter;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public WebSecurityConfiguration(final JwtTokenFilter jwtTokenFilter, final UserRepository userRepository,
                                    final UserMapper userMapper) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceBean()).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public UserDetailsService userDetailsServiceBean() {
        return new UserDetailsServiceImpl(userRepository, userMapper);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/api/v1/login", "/api/v1/newAccessToken", "/api/v1/users/register").permitAll()

                .antMatchers(HttpMethod.POST, "/api/v1/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/v1/bookings").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/v1/users/{id}").hasRole("ADMIN")

                .antMatchers("/api/v1/trainers/{id}/lessons").hasRole("TRAINER")
                .antMatchers(HttpMethod.POST, "/api/v1/lessons").not().hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/api/v1/lessons/{id}").not().hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/api/v1/lessons/{id}").not().hasRole("USER")

                .anyRequest().authenticated()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, authException) -> response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized"))

                .and()
                .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
                .addFilterAfter(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)

                .headers()
                .cacheControl();
    }

}
