package com.agendamedico.spring_security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agendamedico.spring_security.domain.Especialidade;
import com.agendamedico.spring_security.repository.EspecialidadeRepository;

@Service
public class EspecialidadeService {

  @Autowired
  private EspecialidadeRepository especialidadeRepository;

  @Transactional(readOnly = false)
  public void salvar(Especialidade especialidade) {
    especialidadeRepository.save(especialidade);
  }

}
