package com.agendamedico.spring_security.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.agendamedico.spring_security.domain.Usuario;

@Controller
@RequestMapping("/u")
public class UsuarioController {

  // abrir cadastro de usuários (medico/admin/paciente)
  @GetMapping("/novo/cadastro/usuario")
  public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario) {
    return "usuario/cadastro";
  }
}
