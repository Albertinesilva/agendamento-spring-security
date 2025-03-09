package com.agendamedico.spring_security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.agendamedico.spring_security.domain.PerfilTipo;
import com.agendamedico.spring_security.service.UsuarioService;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

  private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
  private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
  private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();

  @Autowired
  private UsuarioService usuarioService;

  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {

    // acessos públicos liberados
    http.authorizeHttpRequests(authorize -> authorize
        .requestMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()
        .requestMatchers("/", "/home").permitAll()

        // acessos privados admin
        .requestMatchers("/u/**").hasAuthority(ADMIN)

        // acessos privados medicos
        .requestMatchers("/medicos/dados", "/medicos/salvar", "/medicos/editar").hasAnyAuthority(MEDICO, ADMIN)
        .requestMatchers("/medicos/**").hasAuthority(MEDICO)

        // acessos privados especialidades
        .requestMatchers("/especialidades/**").hasAuthority(ADMIN)

        // acessos privados pacientes
        .requestMatchers("/pacientes/**").hasAuthority(PACIENTE)

        .anyRequest().authenticated())
        .formLogin(formLogin -> formLogin
            .loginPage("/login")
            .defaultSuccessUrl("/", true)
            .failureUrl("/login-error")
            .permitAll())
        .logout(logout -> logout
            .logoutSuccessUrl("/"));

    // Configuração para tratar acessos negados
    http.exceptionHandling(exception -> exception
        .accessDeniedPage("/acesso-negado"));

    return http.build();

  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      UserDetailsService userDetailsService,
      PasswordEncoder passwordEncoder) {

    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder);

    return new ProviderManager(authProvider);
  }

}
