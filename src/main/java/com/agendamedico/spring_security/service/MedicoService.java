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

  @Transactional(readOnly = false)
  public void salvar(Medico medico) {
    medicoRepository.save(medico);
  }

  /**
   * Edita um médico existente no banco de dados.
   * 
   * <p>
   * Não é necessário chamar {@code medicoRepository.save(medico)} porque o
   * Hibernate
   * mantém a entidade no estado persistente. Qualquer alteração feita no objeto
   * será
   * automaticamente sincronizada com o banco de dados no final da transação.
   * </p>
   *
   * @param medico Objeto {@link Medico} contendo os dados atualizados.
   */
  @Transactional(readOnly = false)
  public void editar(Medico medico) {
    Medico m2 = medicoRepository.findById(medico.getId()).get();
    m2.setCrm(medico.getCrm());
    m2.setDtInscricao(medico.getDtInscricao());
    m2.setNome(medico.getNome());
    if (!medico.getEspecialidades().isEmpty()) {
      m2.getEspecialidades().addAll(medico.getEspecialidades());
    }
  }

  @Transactional(readOnly = true)
  public Medico buscarPorEmail(String email) {
    return medicoRepository.findByUsuarioEmail(email).orElse(new Medico());
  }

}
