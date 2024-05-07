package com.swprojects.agendamento.consultas.repository.projection;

import com.swprojects.agendamento.consultas.domain.Especialidade;
import com.swprojects.agendamento.consultas.domain.Medico;
import com.swprojects.agendamento.consultas.domain.Paciente;

public interface HistoricoPaciente {

	Long getId();
	
	Paciente getPaciente();
	
	String getDataConsulta();
	
	Medico getMedico();
	
	Especialidade getEspecialidade();
}
