package com.agendamedico.spring_security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agendamedico.spring_security.domain.Perfil;
import com.agendamedico.spring_security.domain.Usuario;
import com.agendamedico.spring_security.repository.UsuarioRepository;

@Service
public class UsuarioService implements UserDetailsService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Transactional(readOnly = true)
  public Usuario buscarPorEmail(String email) {
    return usuarioRepository.findByEmail(email);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario usuario = buscarPorEmail(username);
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

  /*private String[] getAuthorities(List<Perfil> perfis) {

    // Versão mais enxuta com stream().map():
    return perfis.stream().map(Perfil::getDesc).toArray(String[]::new);

    // Versão com for tradicional
    // String[] authorities = new String[perfis.size()];
    // for (int i = 0; i < perfis.size(); i++) {
    // authorities[i] = perfis.get(i).getDesc();
    // }
    // return authorities;

    // Versão com forEach
    // List<String> authoritiesList = new ArrayList<>();
    // perfis.forEach(perfil -> authoritiesList.add(perfil.getDesc()));
    // return authoritiesList.toArray(new String[0]);

  }*/

}
