package com.agendamedico.spring_security.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.agendamedico.spring_security.domain.Especialidade;
import com.agendamedico.spring_security.service.EspecialidadeService;

@Controller
@RequestMapping("especialidades")
public class EspecialidadeController {
  
  @Autowired
  private EspecialidadeService especialidadeService;

  @GetMapping({"", "/"})
  public String abrir(Especialidade especialidade) {
    return "especialidade/especialidade";
  }
}
