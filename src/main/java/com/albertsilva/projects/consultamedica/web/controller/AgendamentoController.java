package com.albertsilva.projects.consultamedica.web.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.albertsilva.projects.consultamedica.model.entities.Agendamento;
import com.albertsilva.projects.consultamedica.model.entities.Especialidade;
import com.albertsilva.projects.consultamedica.model.entities.Paciente;
import com.albertsilva.projects.consultamedica.security.model.enums.PerfilTipo;
import com.albertsilva.projects.consultamedica.services.AgendamentoService;
import com.albertsilva.projects.consultamedica.services.EspecialidadeService;
import com.albertsilva.projects.consultamedica.services.PacienteService;

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

  // abre a pagina de agendamento de consultas para o paciente
  @PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
  @GetMapping({ "/agendar" })
  public String agendarConsulta(Agendamento agendamento) {
    return "agendamento/cadastro";
  }

  // buscar os horarios livres, ou seja, que não foram agendados
  @PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
  @GetMapping("/horario/medico/{id}/data/{data}")
  public ResponseEntity<?> getHorarios(@PathVariable("id") Long id,
      @PathVariable("data") @DateTimeFormat(iso = ISO.DATE) LocalDate data) {
    return ResponseEntity.ok(agendamentoService.buscarHorariosNaoAgendadosPorMedicoIdEData(id, data));
  }

  // salvar uma consulta agendada
  @PreAuthorize("hasAuthority('PACIENTE')")
  @PostMapping("/salvar")
  public String salvar(Agendamento agendamento, RedirectAttributes attr, @AuthenticationPrincipal User user) {
    Optional<Paciente> pacienteOptional = pacienteService.buscarPorUsuarioEmail(user.getUsername());

    if (pacienteOptional.isEmpty()) {
      attr.addFlashAttribute("falha", "Você precisa completar seu cadastro antes de agendar uma consulta.");
      return "redirect:/pacientes/dados";
    }
    Paciente paciente = pacienteOptional.get();
    Especialidade especialidade = especialidadeService
        .buscarPorTitulos(new String[] { agendamento.getEspecialidade().getTitulo() })
        .stream().findFirst().orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));

    agendamento.setPaciente(paciente);
    agendamento.setEspecialidade(especialidade);

    return agendamentoService.processarAgendamento(paciente, agendamento, attr);
  }

  @PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
  @GetMapping({ "/historico/paciente", "historico/consultas" })
  public String historico() {
    return "agendamento/historico-paciente";
  }

  // localizar o historico de agendamentos por usuário logado
  @PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
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

  // localizar agendamento pelo id e envia-lo para a pagina de cadastro
  @PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
  @GetMapping("/editar/consulta/{id}")
  public String preEditarConsultaPaciente(@PathVariable("id") Long id, ModelMap model,
      @AuthenticationPrincipal User user) {
    Agendamento agendamento = agendamentoService.buscarPorIdEUsuario(id, user.getUsername());
    model.addAttribute("agendamento", agendamento);
    return "agendamento/cadastro";
  }

  @PreAuthorize("hasAnyAuthority('PACIENTE', 'MEDICO')")
  @PostMapping("/editar")
  public String editarConsulta(Agendamento agendamento, RedirectAttributes attr, @AuthenticationPrincipal User user) {
    String titulo = agendamento.getEspecialidade().getTitulo();
    Especialidade especialidade = especialidadeService.buscarPorTitulos(new String[] { titulo }).stream().findFirst()
        .get();
    agendamento.setEspecialidade(especialidade);

    agendamentoService.editar(agendamento, user.getUsername());
    attr.addFlashAttribute("sucesso", "Sua consulta foi alterada com sucesso.");
    return "redirect:/agendamentos/agendar";
  }

  @PreAuthorize("hasAuthority('PACIENTE')")
  @GetMapping("/excluir/consulta/{id}")
  public String excluirConsulta(@PathVariable("id") Long id, RedirectAttributes attr) {
    agendamentoService.remover(id);
    attr.addFlashAttribute("sucesso", "Consulta excluída com sucesso.");
    return "redirect:/agendamentos/historico/paciente";
  }
}
