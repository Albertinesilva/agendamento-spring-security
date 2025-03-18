package com.agendamedico.spring_security.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.agendamedico.spring_security.datatables.Datatables;
import com.agendamedico.spring_security.datatables.DatatablesColunas;
import com.agendamedico.spring_security.domain.Agendamento;
import com.agendamedico.spring_security.domain.Horario;
import com.agendamedico.spring_security.domain.Paciente;
import com.agendamedico.spring_security.repository.AgendamentoRepository;
import com.agendamedico.spring_security.repository.projection.HistoricoPaciente;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AgendamentoService {

  @Autowired
  private AgendamentoRepository agendamentoRepository;

  @Autowired
  private Datatables datatables;

  @Transactional(readOnly = true)
  public List<Horario> buscarHorariosNaoAgendadosPorMedicoIdEData(Long id, LocalDate data) {
    return agendamentoRepository.findByMedicoIdAndData(id, data);
  }

  @Transactional(readOnly = false)
  public void salvar(Agendamento agendamento) {
    agendamentoRepository.save(agendamento);
  }

  @Transactional(readOnly = true)
  public Map<String, Object> buscarHistoricoPorPacienteEmail(String email, HttpServletRequest request) {
    datatables.setRequest(request);
    datatables.setColunas(DatatablesColunas.AGENDAMENTOS);
    Page<HistoricoPaciente> page = agendamentoRepository.findHistoricoByPacienteEmail(email, datatables.getPageable());
    return datatables.getResponse(page);
  }

  @Transactional(readOnly = true)
  public Map<String, Object> buscarHistoricoPorMedicoEmail(String email, HttpServletRequest request) {
    datatables.setRequest(request);
    datatables.setColunas(DatatablesColunas.AGENDAMENTOS);
    Page<HistoricoPaciente> page = agendamentoRepository.findHistoricoByMedicoEmail(email, datatables.getPageable());
    return datatables.getResponse(page);
  }

  public Agendamento buscarPorId(Long id) {
    return agendamentoRepository.findById(id).get();
  }

  public String processarAgendamento(Paciente paciente, Agendamento agendamento, RedirectAttributes attr) {
    boolean existeAgendamento = paciente.getAgendamentos().stream()
        .anyMatch(a -> a.getDataConsulta().equals(agendamento.getDataConsulta()) &&
            a.getHorario().getId().equals(agendamento.getHorario().getId()));

    return existeAgendamento
        ? processarFalha(attr)
        : processarSucesso(agendamento, attr);
  }

  private String processarFalha(RedirectAttributes attr) {
    attr.addFlashAttribute("falha", "Você já tem uma consulta agendada nesse horário.");
    return "redirect:/agendamentos/agendar";
  }

  private String processarSucesso(Agendamento agendamento, RedirectAttributes attr) {
    salvar(agendamento);
    attr.addFlashAttribute("sucesso", "Sua consulta foi agendada com sucesso.");
    return "redirect:/agendamentos/agendar";
  }

}
