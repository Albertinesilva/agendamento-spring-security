package com.agendamedico.spring_security.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.agendamedico.spring_security.domain.Medico;

@Controller
@RequestMapping("/medicos")
public class MedicoController {

  // abrir pagina de dados pessoais de medicos pelo MEDICO
  @RequestMapping("/dados")
  public String abrirPorMedico(Medico medico, ModelMap model) {

    return "medico/cadastro";
  }

}
