package com.albertsilva.projects.consultamedica.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.albertsilva.projects.consultamedica.model.entities.Paciente;
import com.albertsilva.projects.consultamedica.security.model.entities.Usuario;
import com.albertsilva.projects.consultamedica.security.service.UsuarioService;
import com.albertsilva.projects.consultamedica.services.PacienteService;

@Controller
@RequestMapping("/pacientes")
public class PacienteController {

  @Autowired
  private PacienteService pacienteService;

  @Autowired
  private UsuarioService usuarioService;

  // abrir pagina de dados pessoais do paciente
  @GetMapping("/dados")
  public String cadastrar(ModelMap model, @AuthenticationPrincipal User user) {
    Paciente paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername())
        .orElseGet(() -> new Paciente(new Usuario(user.getUsername())));
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

  // editar o form de dados pessoais do paciente com verificação de senha
  @PostMapping("/editar")
  public String editar(Paciente paciente, ModelMap model, @AuthenticationPrincipal User user) {
    Usuario usuario = usuarioService.buscarPorEmail(user.getUsername());
    if (UsuarioService.isSenhaCorreta(paciente.getUsuario().getSenha(), usuario.getSenha())) {
      pacienteService.editar(paciente);
      model.addAttribute("sucesso", "Seus dados foram editados com sucesso.");
    } else {
      model.addAttribute("falha", "Sua senha não confere, tente novamente.");
    }
    return "paciente/cadastro";
  }
}
