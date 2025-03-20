package com.agendamedico.spring_security.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agendamedico.spring_security.datatables.Datatables;
import com.agendamedico.spring_security.datatables.DatatablesColunas;
import com.agendamedico.spring_security.domain.Perfil;
import com.agendamedico.spring_security.domain.PerfilTipo;
import com.agendamedico.spring_security.domain.Usuario;
import com.agendamedico.spring_security.repository.UsuarioRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UsuarioService implements UserDetailsService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private Datatables datatables;

  /**
   * Busca um usuário por seu email.
   * 
   * @param email O email do usuário a ser buscado.
   * @return O usuário encontrado com o email informado.
   */
  @Transactional(readOnly = true)
  public Usuario buscarPorEmail(String email) {
    return usuarioRepository.findByEmail(email);
  }

  /**
   * Carrega os detalhes do usuário baseado no nome de usuário (email).
   * 
   * @param username O nome de usuário (email).
   * @return Os detalhes do usuário, incluindo suas permissões.
   * @throws UsernameNotFoundException Se o usuário não for encontrado.
   */
  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario usuario = buscarPorEmailEAtivo(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário " + username + " não encontrado."));
    // return new User(
    // usuario.getEmail(),
    // usuario.getSenha(),
    // AuthorityUtils.createAuthorityList(getAuthorities(usuario.getPerfis()))
    // );

    // Versão mais enxuta com stream().map():
    return new User(
        usuario.getEmail(),
        usuario.getSenha(),
        usuario.getPerfis().stream()
            .map(Perfil::getDesc)
            .map(SimpleGrantedAuthority::new)
            .toList());
  }

  /*
   * private String[] getAuthorities(List<Perfil> perfis) {
   * 
   * // Versão mais enxuta com stream().map():
   * return perfis.stream().map(Perfil::getDesc).toArray(String[]::new);
   * 
   * // Versão com for tradicional
   * // String[] authorities = new String[perfis.size()];
   * // for (int i = 0; i < perfis.size(); i++) {
   * // authorities[i] = perfis.get(i).getDesc();
   * // }
   * // return authorities;
   * 
   * // Versão com forEach
   * // List<String> authoritiesList = new ArrayList<>();
   * // perfis.forEach(perfil -> authoritiesList.add(perfil.getDesc()));
   * // return authoritiesList.toArray(new String[0]);
   * 
   * }
   */

  /**
   * Método para buscar os usuários com base nos filtros de pesquisa e paginação.
   * 
   * @param request A requisição HTTP contendo os parâmetros para a pesquisa.
   * @return Um mapa contendo os dados da página de usuários, incluindo as colunas
   *         e o total.
   */
  @Transactional(readOnly = true)
  public Map<String, Object> buscarUsuarios(HttpServletRequest request) {
    datatables.setRequest(request);
    datatables.setColunas(DatatablesColunas.USUARIOS);
    Page<Usuario> page = datatables.getSearch().isEmpty()
        ? usuarioRepository.findAll(datatables.getPageable())
        : usuarioRepository.findByEmailOrPerfil(datatables.getSearch(), datatables.getPageable());
    return datatables.getResponse(page);
  }

  /**
   * Salva um novo usuário no banco de dados.
   * 
   * @param usuario O usuário a ser salvo.
   */
  @Transactional(readOnly = false)
  public void salvarUsuario(Usuario usuario) {
    String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
    usuario.setSenha(crypt);
    usuarioRepository.save(usuario);
  }

  /**
   * Busca um usuário pelo seu ID.
   * 
   * @param id O ID do usuário a ser buscado.
   * @return O usuário encontrado com o ID informado.
   */
  @Transactional(readOnly = true)
  public Usuario buscarPorId(Long id) {
    return usuarioRepository.findById(id).get();
  }

  /**
   * Busca um usuário pelo seu ID e pelos perfis associados a ele.
   * 
   * @param usuarioId O ID do usuário.
   * @param perfisId  Os IDs dos perfis associados ao usuário.
   * @return O usuário encontrado.
   * @throws UsernameNotFoundException Se o usuário não for encontrado com o ID e
   *                                   os perfis informados.
   */
  @Transactional(readOnly = true)
  public Usuario buscarPorIdEPerfis(Long usuarioId, Long[] perfisId) {
    return usuarioRepository.findByIdAndPerfis(usuarioId, perfisId)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário inexistente!"));
  }

  /**
   * Verifica se a senha fornecida corresponde à senha armazenada no banco de
   * dados.
   * 
   * @param senhaDigitada   A senha fornecida pelo usuário.
   * @param senhaArmazenada A senha armazenada no banco de dados.
   * @return {@code true} se as senhas coincidirem, caso contrário {@code false}.
   */
  public static boolean isSenhaCorreta(String senhaDigitada, String senhaArmazenada) {
    return new BCryptPasswordEncoder().matches(senhaDigitada, senhaArmazenada);
  }

  /**
   * Altera a senha de um usuário.
   * 
   * @param usuarioLogado O usuário que está alterando a senha.
   * @param senha1        A nova senha a ser definida.
   */
  @Transactional(readOnly = false)
  public void alterarSenha(Usuario usuarioLogado, String senha1) {
    usuarioLogado.setSenha(new BCryptPasswordEncoder().encode(senha1));
    usuarioRepository.save(usuarioLogado);
  }

  @Transactional(readOnly = false)
  public void salvarCadastroPaciente(Usuario usuario) {
    String crypt = new BCryptPasswordEncoder().encode(usuario.getSenha());
    usuario.setSenha(crypt);
    usuario.addPerfil(PerfilTipo.PACIENTE);
    usuarioRepository.save(usuario);
  }

  @Transactional(readOnly = true)
  public Optional<Usuario> buscarPorEmailEAtivo(String email) {
    return usuarioRepository.findByEmailAndAtivo(email);
  }

}
