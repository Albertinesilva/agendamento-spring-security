package com.agendamedico.spring_security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.agendamedico.spring_security.domain.Medico;
import com.agendamedico.spring_security.service.MedicoService;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

  @Autowired
  private MedicoService medicoService;

  // abrir pagina de dados pessoais de medicos pelo MEDICO
  @GetMapping("/dados")
  public String abrirPorMedico(Medico medico, ModelMap model) {
    return "medico/cadastro";
  }

  // salvar dados pessoais de medicos pelo MEDICO
  @PostMapping("/salvar")
  public String salvar(Medico medico, RedirectAttributes attr) {
    medicoService.salvar(medico);
    attr.addFlashAttribute("success", "Medico inserido com sucesso.");
    attr.addFlashAttribute("medico", medico);
    return "redirect:/medicos/dados";
  }

  // editar dados pessoais de medicos pelo MEDICO
  @PostMapping("/editar")
  public String editar(Medico medico, RedirectAttributes attr) {
    medicoService.editar(medico);
    attr.addFlashAttribute("success", "Operação realizada com sucesso.");
    attr.addFlashAttribute("medico", medico);
    return "redirect:/medicos/dados";
  }

}
