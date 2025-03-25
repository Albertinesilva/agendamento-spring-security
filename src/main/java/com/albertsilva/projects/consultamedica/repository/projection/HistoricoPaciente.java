package com.albertsilva.projects.consultamedica.repository.projection;

import com.albertsilva.projects.consultamedica.model.entities.Especialidade;
import com.albertsilva.projects.consultamedica.model.entities.Medico;
import com.albertsilva.projects.consultamedica.model.entities.Paciente;

public interface HistoricoPaciente {

  Long getId();

  Paciente getPaciente();

  String getDataConsulta();

  Medico getMedico();

  Especialidade getEspecialidade();
}
