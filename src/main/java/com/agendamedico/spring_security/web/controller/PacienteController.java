package com.agendamedico.spring_security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.agendamedico.spring_security.domain.Paciente;
import com.agendamedico.spring_security.domain.Usuario;
import com.agendamedico.spring_security.service.PacienteService;
import com.agendamedico.spring_security.service.UsuarioService;

@Controller
@RequestMapping("pacientes")
public class PacienteController {

  @Autowired
  private PacienteService pacienteService;

  @Autowired
  private UsuarioService usuarioService;

  // abrir pagina de dados pessoais do paciente
  @GetMapping("/dados")
  public String cadastrar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {
    paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());
    if (paciente.hasNotId()) {
      paciente.setUsuario(new Usuario(user.getUsername()));
    }
    model.addAttribute("paciente", paciente);
    return "paciente/cadastro";
  }

  // salvar o form de dados pessoais com verificacao de senha
  @PostMapping("/salvar")
  public String salvar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {
    Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
    if (UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(), usuario.getSenha())) {
      paciente.setUsuario(usuario);
      pacienteService.salvar(paciente);
      model.addAttribute("sucesso", "Seus dados foram inseridos com sucesso.");
    } else {
      model.addAttribute("falha", "Sua senha não confere, tente novamente.");
    }
    return "paciente/cadastro";
  }
}
