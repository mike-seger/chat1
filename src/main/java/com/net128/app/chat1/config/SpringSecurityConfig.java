package com.net128.app.chat1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.inject.Inject;

@Configuration
@EnableWebSecurity
@Profile({"dev", "test"})
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${swagger.uris:}")
    private String [] swaggerUris;

    @Value("${open.endpoint.uris:}")
    private String [] openEndpointUris;

    @Value("${spring.h2.console.path:}")
    private String h2ConsolePath;

    @Inject
    private UserConfig userConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().authorizeRequests()
            .antMatchers(swaggerUris).permitAll()
            .antMatchers(openEndpointUris).permitAll()
            .anyRequest().authenticated()
            .antMatchers(h2ConsolePath+"/**").permitAll()
            .and().httpBasic()
            ;

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        InMemoryUserDetailsManagerConfigurer conf=auth.inMemoryAuthentication();
        for(UserConfig.User user : userConfig.getUsers()) {
            conf.withUser(user.getName()).password(user.getPassword()).roles(user.getRoles());
        }
    }
}
