package com.agendamedico.spring_security.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.agendamedico.spring_security.domain.Especialidade;

@Repository
public interface EspecialidadeRepository extends JpaRepository<Especialidade, Long> {

  @Query("select e from Especialidade e where e.titulo like :search%")
  Page<?> findAllByTitulo(String search, Pageable pageable);

  @Query("select e.titulo from Especialidade e where e.titulo like :termo%")
  List<String> findEspecialidadesByTermo(String termo);

}