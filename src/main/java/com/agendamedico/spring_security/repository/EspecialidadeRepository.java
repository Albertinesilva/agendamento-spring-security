package com.agendamedico.spring_security.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.agendamedico.spring_security.domain.Especialidade;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {

  /**
   * Busca todas as especialidades com base no título, realizando a busca com o
   * prefixo fornecido.
   *
   * @param search   Prefixo do título da especialidade.
   * @param pageable Objeto de paginação.
   * @return Uma página de especialidades que correspondem ao título fornecido.
   */
  @Query("select e from Especialidade e where e.titulo like :search%")
  Page<Especialidade> findAllByTitulo(String search, Pageable pageable);

  /**
   * Busca os títulos das especialidades que começam com o termo fornecido.
   *
   * @param termo O termo a ser pesquisado.
   * @return Uma lista de títulos de especialidades que começam com o termo
   *         fornecido.
   */
  @Query("select e.titulo from Especialidade e where e.titulo like :termo%")
  List<String> findEspecialidadesByTermo(String termo);

  /**
   * Busca especialidades cujos títulos estão na lista fornecida.
   *
   * @param titulos Array de títulos das especialidades.
   * @return Um conjunto de especialidades com os títulos fornecidos.
   */
  @Query("select e from Especialidade e where e.titulo in :titulos")
  Set<Especialidade> findByTitulos(String[] titulos);

  /**
   * Busca as especialidades associadas a um médico específico, dado o seu id.
   *
   * @param id       ID do médico.
   * @param pageable Objeto de paginação.
   * @return Uma página de especialidades associadas ao médico especificado.
   */
  @Query("select e from Especialidade e "
      + "join e.medicos m "
      + "where m.id = :id")
  Page<Especialidade> findByIdMedico(Long id, Pageable pageable);

}
