package com.agendamedico.spring_security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.agendamedico.spring_security.domain.Medico;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {

  /**
   * Busca um médico pelo ID do usuário associado a ele.
   *
   * @param id ID do usuário associado ao médico.
   * @return Um objeto Optional contendo o médico encontrado, ou vazio se não
   *         encontrado.
   */
  @Query("select m from Medico m where m.usuario.id = :id")
  Optional<Medico> findByUsuarioId(Long id);

  /**
   * Busca um médico pelo e-mail do usuário associado a ele.
   *
   * @param email E-mail do usuário associado ao médico.
   * @return Um objeto Optional contendo o médico encontrado, ou vazio se não
   *         encontrado.
   */
  @Query("select m from Medico m where m.usuario.email like :email")
  Optional<Medico> findByUsuarioEmail(String email);

}
