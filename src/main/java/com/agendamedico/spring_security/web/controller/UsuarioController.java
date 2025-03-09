package com.agendamedico.spring_security.web.controller;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.agendamedico.spring_security.domain.Medico;
import com.agendamedico.spring_security.domain.Perfil;
import com.agendamedico.spring_security.domain.PerfilTipo;
import com.agendamedico.spring_security.domain.Usuario;
import com.agendamedico.spring_security.service.MedicoService;
import com.agendamedico.spring_security.service.UsuarioService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/u")
public class UsuarioController {

  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private MedicoService medicoService;

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

  // salvar cadastro de usuários por administrador
  @PostMapping("/cadastro/salvar")
  public String salvarUsuarios(Usuario usuario, RedirectAttributes attr) {

    if (usuario.getPerfis().size() > 2 ||
        usuario.getPerfis().stream().map(Perfil::getId).collect(Collectors.toSet())
            .containsAll(Arrays.asList(1L, 3L))
        ||
        usuario.getPerfis().stream().map(Perfil::getId).collect(Collectors.toSet())
            .containsAll(Arrays.asList(2L, 3L))) {
      attr.addFlashAttribute("falha", "Paciente não pode ser Admin e/ou Médico!");
      attr.addFlashAttribute("usuario", usuario);
    } else {
      try {
        usuarioService.salvarUsuario(usuario);
        attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
      } catch (DataIntegrityViolationException ex) {
        attr.addFlashAttribute("falha", "Cadastro não realizado, email já existente!");
      }

    }
    return "redirect:/u/novo/cadastro/usuario";
  }

  // pre edição de credenciais de usuário
  @GetMapping("/editar/credenciais/usuario/{id}")
  public ModelAndView preEditarCredenciais(@PathVariable("id") Long id) {
    ModelAndView mv = new ModelAndView("usuario/cadastro");
    mv.addObject("usuario", usuarioService.buscarPorId(id));
    return mv;
  }

  // pre edição de cadastro de usuários
  @GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
  public ModelAndView preEditarCadastroDadosPessoais(@PathVariable("id") Long usuarioId,
      @PathVariable("perfis") Long[] perfisId) {
    Usuario usuario = usuarioService.buscarPorIdEPerfis(usuarioId, perfisId);

    if (usuario.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) &&
        !usuario.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {
      return new ModelAndView("usuario/cadastro", "usuario", usuario);

    } else if (usuario.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {

      Medico medico = medicoService.buscarPorUsuarioId(usuarioId);
      return medico.hasNotId()
          ? new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(usuarioId)))
          : new ModelAndView("medico/cadastro", "medico", medico);

    } else if (usuario.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))) {
      ModelAndView mv = new ModelAndView("error");
      mv.addObject("status", 403);
      mv.addObject("error", "Área Restrita");
      mv.addObject("message", "Os dados de pacientes são restritos a ele mesmo");
      return mv;
    }
    return new ModelAndView("redirect:/u/lista");

  }
}
