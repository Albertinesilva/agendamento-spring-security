package com.agendamedico.spring_security.repository.projection;

import com.agendamedico.spring_security.domain.Especialidade;
import com.agendamedico.spring_security.domain.Medico;
import com.agendamedico.spring_security.domain.Paciente;

public interface HistoricoPaciente {

  Long getId();

  Paciente getPaciente();

  String getDataConsulta();

  Medico getMedico();

  Especialidade getEspecialidade();
}
