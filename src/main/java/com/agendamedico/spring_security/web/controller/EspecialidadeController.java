package com.agendamedico.spring_security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.agendamedico.spring_security.domain.Especialidade;
import com.agendamedico.spring_security.service.EspecialidadeService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("especialidades")
public class EspecialidadeController {

  @Autowired
  private EspecialidadeService especialidadeService;

  @GetMapping({ "", "/" })
  public String abrir(Especialidade especialidade) {
    return "especialidade/especialidade";
  }

  @PostMapping("/salvar")
  public String salvar(Especialidade especialidade, RedirectAttributes attr) {
    especialidadeService.salvar(especialidade);
    attr.addFlashAttribute("sucesso", "Operação realizada com sucesso.");
    return "redirect:/especialidades";
  }

  @GetMapping("/datatables/server")
  public ResponseEntity<?> getEspecialidades(HttpServletRequest request) {
    return ResponseEntity.ok(especialidadeService.buscarEspecialidades(request));
  }
}
