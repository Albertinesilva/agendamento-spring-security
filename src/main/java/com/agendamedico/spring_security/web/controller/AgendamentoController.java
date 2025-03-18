package com.agendamedico.spring_security.web.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.agendamedico.spring_security.domain.Agendamento;
import com.agendamedico.spring_security.domain.Especialidade;
import com.agendamedico.spring_security.domain.Paciente;
import com.agendamedico.spring_security.domain.PerfilTipo;
import com.agendamedico.spring_security.service.AgendamentoService;
import com.agendamedico.spring_security.service.EspecialidadeService;
import com.agendamedico.spring_security.service.PacienteService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/agendamentos")
public class AgendamentoController {

  @Autowired
  private AgendamentoService agendamentoService;

  @Autowired
  private PacienteService pacienteService;

  @Autowired
  private EspecialidadeService especialidadeService;

  @GetMapping({ "/agendar" })
  public String agendarConsulta(Agendamento agendamento) {
    return "agendamento/cadastro";
  }

  // buscar os horarios livres, ou seja, que não foram agendados
  @GetMapping("/horario/medico/{id}/data/{data}")
  public ResponseEntity<?> getHorarios(@PathVariable("id") Long id,
      @PathVariable("data") @DateTimeFormat(iso = ISO.DATE) LocalDate data) {
    return ResponseEntity.ok(agendamentoService.buscarHorariosNaoAgendadosPorMedicoIdEData(id, data));
  }

  // salvar uma consulta agendada
  @PostMapping({ "/salvar" })
  public String salvar(Agendamento agendamento, RedirectAttributes attr, @AuthenticationPrincipal User user) {
    Paciente paciente = pacienteService.buscarPorUsuarioEmail(user.getUsername());
    String titulo = agendamento.getEspecialidade().getTitulo();
    Especialidade especialidade = especialidadeService.buscarPorTitulos(new String[] { titulo })
        .stream().findFirst().get();
    agendamento.setPaciente(paciente);
    agendamento.setEspecialidade(especialidade);
    agendamentoService.salvar(agendamento);
    attr.addFlashAttribute("sucesso", "Sua consulta foi agendada com sucesso.");
    return "redirect:/agendamentos/agendar";
  }

  @GetMapping({ "/historico/paciente", "historico/consultas" })
  public String historico() {
    return "agendamento/historico-paciente";
  }

  // localizar o historico de agendamentos por usuário logado
  @GetMapping("/datatables/server/historico")
  public ResponseEntity<?> historicoAgendamentosPorPaciente(HttpServletRequest request,
      @AuthenticationPrincipal User user) {
    if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.PACIENTE.getDesc()))) {
      return ResponseEntity.ok(agendamentoService.buscarHistoricoPorPacienteEmail(user.getUsername(), request));
    }

    if (user.getAuthorities().contains(new SimpleGrantedAuthority(PerfilTipo.MEDICO.getDesc()))) {
      return ResponseEntity.ok(agendamentoService.buscarHistoricoPorMedicoEmail(user.getUsername(), request));
    }
    return ResponseEntity.notFound().build();
  }
}
