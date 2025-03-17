package com.agendamedico.spring_security.web.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.agendamedico.spring_security.domain.Agendamento;
import com.agendamedico.spring_security.service.AgendamentoService;

@Controller
@RequestMapping("/agendamentos")
public class AgendamentoController {

  @Autowired
  private AgendamentoService agendamentoService;

  @GetMapping({ "/agendar" })
  public String agendarConsulta(Agendamento agendamento) {
    return "agendamento/cadastro";
  }

  @GetMapping("/horario/medico/{id}/data/{data}")
  public ResponseEntity<?> getHorarios(@PathVariable("id") Long id, @PathVariable("data") @DateTimeFormat(iso = ISO.DATE) LocalDate data) {
    return ResponseEntity.ok(agendamentoService.buscarHorariosNaoAgendadosPorMedicoIdEData(id, data));
  } 
}
