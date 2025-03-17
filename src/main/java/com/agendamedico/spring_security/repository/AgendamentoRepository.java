package com.agendamedico.spring_security.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.agendamedico.spring_security.domain.Agendamento;
import com.agendamedico.spring_security.domain.Horario;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

  @Query("select h "
      + "from Horario h "
      + "where not exists("
      + "select a.horario.id "
      + "from Agendamento a "
      + "where "
      + "a.medico.id = :id and "
      + "a.dataConsulta = :data and "
      + "a.horario.id = h.id "
      + ") "
      + "order by h.horaMinuto asc")
  List<Horario> findByMedicoIdAndData(Long id, LocalDate data);

}
