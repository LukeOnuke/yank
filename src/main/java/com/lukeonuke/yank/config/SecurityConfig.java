package com.lukeonuke.yank.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .logout(l -> l
                        .logoutSuccessUrl("/oauth2/authorization/google").permitAll()
                )
                .csrf(c -> c
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                )
                .csrf().ignoringAntMatchers("/api/v1/server/send").and()
                .antMatcher("/**").authorizeRequests()
                .antMatchers("/css/**", "/js/**", "/api/v1/user/simple").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .and()
                .cors().disable();

    }
}
