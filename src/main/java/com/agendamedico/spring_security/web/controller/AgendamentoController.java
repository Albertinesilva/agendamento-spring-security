package com.agendamedico.spring_security.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.agendamedico.spring_security.domain.Agendamento;

@Controller
@RequestMapping("/agendamentos")
public class AgendamentoController {

  @GetMapping({ "/agendar" })
  public String agendarConsulta(Agendamento agendamento) {

    return "agendamento/cadastro";
  }
}
