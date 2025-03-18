package com.agendamedico.spring_security.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agendamedico.spring_security.domain.Agendamento;
import com.agendamedico.spring_security.domain.Horario;
import com.agendamedico.spring_security.repository.AgendamentoRepository;

@Service
public class AgendamentoService {

  @Autowired
  private AgendamentoRepository agendamentoRepository;

  @Transactional(readOnly = true)
  public List<Horario> buscarHorariosNaoAgendadosPorMedicoIdEData(Long id, LocalDate data) {
    return agendamentoRepository.findByMedicoIdAndData(id, data);
  }

  @Transactional(readOnly = false)
  public void salvar(Agendamento agendamento) {
    agendamentoRepository.save(agendamento);
  }

}
