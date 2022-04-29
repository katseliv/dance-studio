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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletResponse;

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
                .csrf()
                .disable()
                .formLogin()
                .loginPage("/users/login")
                .usernameParameter("email")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/users/login?error")
                .permitAll()

                .and()
                .authorizeRequests()
                .antMatchers("/", "/users/login", "/users/register", "/token").permitAll()

                .antMatchers("/users", "/users/", "/users/create", "/bookings", "/bookings/").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/users/{id}").hasRole("ADMIN")

                .antMatchers("/lessons/create").not().hasRole("USER")
                .antMatchers(HttpMethod.PUT, "/lessons/{id}").not().hasRole("USER")
                .antMatchers(HttpMethod.DELETE, "/lessons/{id}").not().hasRole("USER")
                .antMatchers("/trainers/{id}/lessons").hasRole("TRAINER")

                .anyRequest().authenticated()

                .and()
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, authException) -> response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"))
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout"))
                .permitAll()
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
