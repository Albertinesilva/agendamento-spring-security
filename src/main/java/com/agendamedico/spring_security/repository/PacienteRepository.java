package com.agendamedico.spring_security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.agendamedico.spring_security.domain.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

  @Query("select p from Paciente p where p.usuario.email like :email")
  Optional<Paciente> findByUsuarioEmail(String email);
}
