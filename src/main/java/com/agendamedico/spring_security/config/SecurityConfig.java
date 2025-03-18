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

  private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
  private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
  private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();

  @Autowired
  private UsuarioService usuarioService;

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

    http.authorizeHttpRequests(authorize -> authorize
        // Permite acesso público a arquivos estáticos
        .requestMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()
        .requestMatchers("/", "/home").permitAll()

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

        .anyRequest().authenticated())

        // Configuração de login
        .formLogin(formLogin -> formLogin
            .loginPage("/login") // Página customizada de login
            .defaultSuccessUrl("/", true) // Redireciona após login bem-sucedido
            .failureUrl("/login-error") // Redireciona em caso de falha
            .permitAll())

        // Configuração de logout
        .logout(logout -> logout
            .logoutSuccessUrl("/") // Redireciona para home após logout
            .invalidateHttpSession(true) // Invalida sessão após logout
            .deleteCookies("JSESSIONID")) // Remove cookies de sessão

        // Tratamento de acessos negados
        .exceptionHandling(exception -> exception
            .accessDeniedPage("/acesso-negado"));

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
   * Configura o gerenciador de autenticação do Spring Security.
   * <p>
   * O {@code AuthenticationManager} utiliza um {@code DaoAuthenticationProvider}
   * para buscar usuários e validar suas credenciais.
   * </p>
   * <p>
   * Exemplo de uso:
   * 
   * <pre>
   * {@code
   * AuthenticationManager authManager = authenticationManager(userDetailsService, passwordEncoder);
   * Authentication authentication = authManager
   *     .authenticate(new UsernamePasswordAuthenticationToken("user", "password"));
   * }
   * </pre>
   * </p>
   *
   * @param userDetailsService Serviço que fornece os detalhes do usuário
   *                           autenticado.
   * @param passwordEncoder    Codificador de senhas utilizado para validação.
   * @return Uma instância de {@link AuthenticationManager} gerenciando a
   *         autenticação.
   * @see DaoAuthenticationProvider
   */
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
