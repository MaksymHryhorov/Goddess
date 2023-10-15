package com.java.test.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/api/products/add").hasRole("MANAGER")
                .antMatchers("/api/products/**").permitAll() // Allow access to all endpoints under /api/products
                .antMatchers("/api/registration/**").permitAll() // Allow access to all endpoints under /api/registration
                .anyRequest().authenticated(); // Require authentication for all other endpoints

        // Disable CSRF for simplicity (you may want to enable it in a production environment)
        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Configure authentication provider (e.g., in-memory, JDBC, or custom)
    }
}

