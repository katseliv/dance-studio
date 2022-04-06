package com.dataart.dancestudio.configuration;

import com.dataart.dancestudio.mapper.UserMapper;
import com.dataart.dancestudio.repository.UserRepositoryImpl;
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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final UserRepositoryImpl userRepositoryImpl;
    private final UserMapper userMapper;

    @Autowired
    public WebSecurityConfiguration(final UserRepositoryImpl userRepositoryImpl, final UserMapper userMapper) {
        this.userRepositoryImpl = userRepositoryImpl;
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
        return new UserDetailsServiceImpl(userRepositoryImpl, userMapper);
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
                .defaultSuccessUrl("/operations")
                .failureUrl("/users/login?error")
                .permitAll()

                .and()
                .authorizeRequests()
                .antMatchers("/", "/operations", "/users/login", "/users/register").permitAll()

                .antMatchers("/users/**", "/trainers/**", "/bookings/**", "/lessons/**").hasRole("ADMIN")
                .antMatchers("/trainers/**").hasRole("TRAINER")

                .antMatchers("/bookings/create").hasAnyRole("TRAINER", "USER")
                .antMatchers(HttpMethod.GET, "/users/*", "/users/*/update", "users/*/bookings",
                        "/bookings/*", "/bookings/*/update", "/lessons/*").hasAnyRole("TRAINER", "USER")
                .antMatchers(HttpMethod.PUT, "/users/*", "/bookings/*").hasAnyRole("TRAINER", "USER")
                .antMatchers(HttpMethod.DELETE, "/bookings/*").hasAnyRole("TRAINER", "USER")

                .antMatchers("/lessons/create").hasAnyRole("TRAINER")
                .antMatchers(HttpMethod.GET, "/lessons/*/update").hasAnyRole("TRAINER")
                .antMatchers(HttpMethod.PUT, "/lessons/*").hasAnyRole("TRAINER")
                .antMatchers(HttpMethod.DELETE, "/lessons/*").hasAnyRole("TRAINER")

                .anyRequest().authenticated()


                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout"))
                .permitAll()
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID");
    }

}
