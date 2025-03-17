package com.agendamedico.spring_security.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agendamedico.spring_security.domain.Medico;
import com.agendamedico.spring_security.repository.MedicoRepository;

@Service
public class MedicoService {

  @Autowired
  private MedicoRepository medicoRepository;

  /**
   * Busca um médico pelo ID do usuário associado.
   *
   * @param id O ID do usuário.
   * @return O médico associado ao usuário, ou um novo médico caso não seja
   *         encontrado.
   */
  @Transactional(readOnly = false)
  public Medico buscarPorUsuarioId(Long id) {
    return medicoRepository.findByUsuarioId(id).orElse(new Medico());
  }

  /**
   * Salva um médico no banco de dados.
   *
   * @param medico O médico a ser salvo.
   */
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
   * será automaticamente sincronizada com o banco de dados no final da transação.
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

  /**
   * Busca um médico pelo e-mail do usuário associado.
   *
   * @param email O e-mail do usuário.
   * @return O médico associado ao usuário, ou um novo médico caso não seja
   *         encontrado.
   */
  @Transactional(readOnly = true)
  public Medico buscarPorEmail(String email) {
    return medicoRepository.findByUsuarioEmail(email).orElse(new Medico());
  }

  /**
   * Exclui uma especialização de um médico.
   *
   * @param idMed O ID do médico.
   * @param idEsp O ID da especialização a ser removida.
   */
  @Transactional(readOnly = false)
  public void excluirEspecializacao(Long idMed, Long idEsp) {
    Medico medico = medicoRepository.findById(idMed).get();
    medico.getEspecialidades().removeIf(e -> e.getId().equals(idEsp));
  }

  @Transactional(readOnly = true)
  public List<Medico> buscarMedicosPorEspecialidade(String titulo) {
    return medicoRepository.findByMedicosPorEspecialidade(titulo);
  }

}
