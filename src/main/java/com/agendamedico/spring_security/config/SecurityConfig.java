package com.agendamedico.spring_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {

    http.authorizeRequests(authorizeRequests -> authorizeRequests
        .requestMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()
        .requestMatchers("/", "/home").permitAll()
        .anyRequest().authenticated())
        .formLogin(formLogin -> formLogin
            .loginPage("/login")
            .permitAll());
    return http.build();

  }

}
