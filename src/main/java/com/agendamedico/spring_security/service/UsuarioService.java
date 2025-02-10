package com.agendamedico.spring_security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agendamedico.spring_security.domain.Usuario;
import com.agendamedico.spring_security.repository.UsuarioRepository;

@Service
public class UsuarioService {

  @Autowired
  private UsuarioRepository usuarioRepository;

  public Usuario buscarPorEmail(String email) {
    return usuarioRepository.findByEmail(email);
  }

}
