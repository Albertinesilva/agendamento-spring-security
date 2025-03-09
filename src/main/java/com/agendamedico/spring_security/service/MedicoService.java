package com.agendamedico.spring_security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agendamedico.spring_security.domain.Medico;
import com.agendamedico.spring_security.repository.MedicoRepository;

@Service
public class MedicoService {

  @Autowired
  private MedicoRepository medicoRepository;

  @Transactional(readOnly = false)
  public Medico buscarPorUsuarioId(Long id) {
    return medicoRepository.findByUsuarioId(id).orElse(new Medico());
  }
}
