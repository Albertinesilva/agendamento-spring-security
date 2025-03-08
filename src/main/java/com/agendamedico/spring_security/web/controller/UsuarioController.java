package com.agendamedico.spring_security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.agendamedico.spring_security.domain.Usuario;
import com.agendamedico.spring_security.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/u")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;

  // abrir cadastro de usuários (medico/admin/paciente)
  @GetMapping("/novo/cadastro/usuario")
  public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario) {
    return "usuario/cadastro";
  }

  // abrir lista de usuários
  @GetMapping("/lista")
  public String listaUsuarios() {
    return "usuario/lista";
  }

  // listar usuários na datatables
  @GetMapping("/datatables/server/usuarios")
  public ResponseEntity<?> listaUsuariosDatatables(HttpServletRequest request) {
    return ResponseEntity.ok(usuarioService.buscarUsuarios(request));
  }
}
