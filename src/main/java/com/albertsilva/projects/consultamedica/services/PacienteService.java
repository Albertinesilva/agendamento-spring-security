package com.albertsilva.projects.consultamedica.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.albertsilva.projects.consultamedica.model.entities.Paciente;
import com.albertsilva.projects.consultamedica.repository.PacienteRepository;

@Service
public class PacienteService {

  @Autowired
  private PacienteRepository pacienteRepository;

  @Transactional(readOnly = true)
  public Optional<Paciente> buscarPorUsuarioEmail(String email) {
    return pacienteRepository.findByUsuarioEmail(email);
  }

  @Transactional(readOnly = false)
  public void salvar(Paciente paciente) {
    pacienteRepository.save(paciente);
  }

  @Transactional(readOnly = false)
  public void editar(Paciente paciente) {
    Paciente p2 = pacienteRepository.findById(paciente.getId()).get();
    p2.setNome(paciente.getNome());
    p2.setDtNascimento(paciente.getDtNascimento());
    pacienteRepository.save(p2);
  }
}
