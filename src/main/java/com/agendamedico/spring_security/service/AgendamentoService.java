package com.agendamedico.spring_security.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agendamedico.spring_security.datatables.Datatables;
import com.agendamedico.spring_security.datatables.DatatablesColunas;
import com.agendamedico.spring_security.domain.Agendamento;
import com.agendamedico.spring_security.domain.Horario;
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

  public boolean existeAgendamentoParaPacienteNoMesmoHorario(Long pacienteId, LocalDate data, Long horarioId) {
    return agendamentoRepository.existsByPacienteAndDataAndHorario(pacienteId, data, horarioId);
  }

}
