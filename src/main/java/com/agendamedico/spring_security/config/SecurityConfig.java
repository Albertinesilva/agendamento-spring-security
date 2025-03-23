package com.agendamedico.spring_security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.agendamedico.spring_security.domain.PerfilTipo;

/**
 * Classe de configuração de segurança do Spring Security.
 * <p>
 * Define as permissões de acesso para diferentes tipos de usuários,
 * configura a autenticação e o gerenciamento de senhas.
 * </p>
 * <p>
 * Usuários podem acessar páginas públicas sem autenticação, enquanto rotas
 * específicas
 * exigem permissões baseadas nos perfis {@code ADMIN}, {@code MEDICO} e
 * {@code PACIENTE}.
 * </p>
 */
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

  @Value("${app.security.remember-me-key}")
  private String rememberMeKey;

  private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
  private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
  private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();

  /**
   * Configura a cadeia de filtros de segurança para controle de acessos.
   * <p>
   * - Permite acesso público a arquivos estáticos (CSS, JS, imagens).
   * - Restringe rotas específicas com base no perfil do usuário.
   * - Configura login, logout e tratamento de erros de permissão.
   * </p>
   *
   * @param http Objeto {@link HttpSecurity} usado para configurar as regras de
   *             segurança.
   * @return Uma instância de {@link SecurityFilterChain} contendo a configuração.
   * @throws Exception Caso ocorra um erro ao configurar a segurança.
   */
  @Bean
  public SecurityFilterChain configure(HttpSecurity http) throws Exception {

    http.authorizeHttpRequests((authorize) -> authorize
        // Permite acesso público a arquivos estáticos
        .requestMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()
        .requestMatchers("/", "/home", "/expired").permitAll()
        .requestMatchers("/u/novo/cadastro", "/u/cadastro/realizado", "/u/cadastro/paciente/salvar").permitAll()
        .requestMatchers("/u/confirmacao/cadastro").permitAll()
        .requestMatchers("/u/p/**").permitAll()

        // Permissões para Administradores
        .requestMatchers("/u/editar/senha", "/u/confirmar/senha").hasAnyAuthority(MEDICO, PACIENTE)
        .requestMatchers("/u/**").hasAuthority(ADMIN)

        // Permissões para Médicos
        .requestMatchers("/medicos/especialidade/titulo/*").hasAnyAuthority(MEDICO, PACIENTE)
        .requestMatchers("/medicos/dados", "/medicos/salvar", "/medicos/editar").hasAnyAuthority(ADMIN, MEDICO)
        .requestMatchers("/medicos/**").hasAuthority(MEDICO)

        // Permissões para Pacientes
        .requestMatchers("/pacientes/**").hasAuthority(PACIENTE)

        // Permissões para Especialidades
        .requestMatchers("/especialidades/datatables/server/medico/*").hasAnyAuthority(ADMIN, MEDICO)
        .requestMatchers("/especialidades/titulo").hasAnyAuthority(ADMIN, MEDICO, PACIENTE)
        .requestMatchers("/especialidades/**").hasAnyAuthority(ADMIN)

        .anyRequest().authenticated() // Restringe acesso a rotas não especificadas
    )

        // Configuração de login
        .formLogin(formLogin -> formLogin
            .loginPage("/login") // Página customizada de login
            .defaultSuccessUrl("/", true) // Redireciona após login bem-sucedido
            .failureUrl("/login-error") // Redireciona em caso de falha
            .permitAll() // Permite acesso a todos os usuários
        )
        // Configuração de logout
        .logout(logout -> logout
            .logoutSuccessUrl("/") // Redireciona para home após logout
            .invalidateHttpSession(true) // Invalida sessão após logout
            .deleteCookies("JSESSIONID")// Remove cookies de sessão
        )
        // Tratamento de acessos negados
        .exceptionHandling(exception -> exception
            .accessDeniedPage("/acesso-negado") // Página customizada de acesso negado
        )
        // Configuração de sessão e cookies de autenticação
        .rememberMe(rememberMe -> rememberMe
            .key(rememberMeKey) // Opcional, define uma chave única
            .tokenValiditySeconds(86400) // Tempo de validade do token (1 dia)
        )
        // Configuração de sessão (separada de rememberMe)
        .sessionManagement((session) -> session
            .maximumSessions(1) // Número máximo de sessões
            .expiredUrl("/expired") // Página de sessão expirada
            .maxSessionsPreventsLogin(false) // Permite login simultâneo
            .sessionRegistry(sessionRegistry()) // Registra sessões ativas
        )
        // Configuração de sessão (separada de rememberMe)
        .sessionManagement((session) -> session
            .sessionFixation() // Proteção contra ataques de fixação de sessão
            .newSession() // Cria uma nova sessão
            .sessionAuthenticationStrategy(sessionAuthStrategy()) // Estratégia de autenticação de sessão
        );
    return http.build();
  }

  /**
   * Define um bean para codificação de senhas usando
   * {@link BCryptPasswordEncoder}.
   * <p>
   * O {@code BCryptPasswordEncoder} gera um hash seguro e é recomendado para
   * armazenamento de senhas no banco de dados.
   * </p>
   *
   * @return Uma instância de {@link PasswordEncoder} que usa o algoritmo BCrypt.
   * @see BCryptPasswordEncoder
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Define um bean para o {@link AuthenticationManager}, utilizando a
   * configuração automática do Spring Security.
   * 
   * <p>
   * Este método obtém e retorna a instância gerenciada de
   * {@link AuthenticationManager}
   * a partir de {@link AuthenticationConfiguration}, permitindo que o Spring
   * descubra automaticamente
   * a implementação de {@link UserDetailsService} presente na aplicação.
   * </p>
   * 
   * @param authenticationConfiguration configuração de autenticação do Spring
   *                                    Security.
   * @return uma instância de {@link AuthenticationManager} gerenciada pelo
   *         Spring.
   * @throws Exception caso ocorra um erro ao obter o
   *                   {@link AuthenticationManager}.
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public SessionAuthenticationStrategy sessionAuthStrategy() {
    return new RegisterSessionAuthenticationStrategy(sessionRegistry());
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  public ServletListenerRegistrationBean<?> servletListenerRegistrationBean() {
    return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
  }

}
