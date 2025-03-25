package com.albertsilva.projects.consultamedica.model.entities;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

/**
 * Representa um agendamento de consulta no sistema.
 * Contém informações sobre a especialidade médica, médico, paciente, horário e
 * data da consulta.
 */
@Entity
@Table(name = "agendamentos")
public class Agendamento extends AbstractEntity {

	/**
	 * Especialidade médica associada ao agendamento.
	 */
	@ManyToOne
	@JoinColumn(name = "id_especialidade")
	private Especialidade especialidade;

	/**
	 * Médico responsável pelo atendimento.
	 */
	@ManyToOne
	@JoinColumn(name = "id_medico")
	private Medico medico;

	/**
	 * Paciente que será atendido no agendamento.
	 */
	@ManyToOne
	@JoinColumn(name = "id_paciente")
	private Paciente paciente;

	/**
	 * Horário da consulta médica.
	 */
	@ManyToOne
	@JoinColumn(name = "id_horario")
	private Horario horario;

	/**
	 * Data da consulta médica.
	 */
	@Column(name = "data_consulta")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dataConsulta;

	public Agendamento() {

	}

	/**
	 * Cria um novo agendamento.
	 * 
	 * @param especialidade a especialidade médica do agendamento.
	 * @param medico        o médico responsável pelo atendimento.
	 * @param paciente      o paciente que será atendido.
	 * @param horario       o horário da consulta médica.
	 * @param dataConsulta  a data da consulta médica.
	 */
	public Agendamento(Especialidade especialidade, Medico medico, Paciente paciente, Horario horario,
			LocalDate dataConsulta) {
		this.especialidade = especialidade;
		this.medico = medico;
		this.paciente = paciente;
		this.horario = horario;
		this.dataConsulta = dataConsulta;
	}

	/**
	 * Obtém a especialidade associada ao agendamento.
	 * 
	 * @return a especialidade médica do agendamento.
	 */
	public Especialidade getEspecialidade() {
		return especialidade;
	}

	/**
	 * Define a especialidade do agendamento.
	 * 
	 * @param especialidade a especialidade a ser associada ao agendamento.
	 */
	public void setEspecialidade(Especialidade especialidade) {
		this.especialidade = especialidade;
	}

	/**
	 * Obtém o médico responsável pelo atendimento.
	 * 
	 * @return o médico do agendamento.
	 */
	public Medico getMedico() {
		return medico;
	}

	/**
	 * Define o médico do agendamento.
	 * 
	 * @param medico o médico a ser associado ao agendamento.
	 */
	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	/**
	 * Obtém o paciente associado ao agendamento.
	 * 
	 * @return o paciente do agendamento.
	 */
	public Paciente getPaciente() {
		return paciente;
	}

	/**
	 * Define o paciente do agendamento.
	 * 
	 * @param paciente o paciente a ser associado ao agendamento.
	 */
	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	/**
	 * Obtém a data da consulta médica.
	 * 
	 * @return a data da consulta.
	 */
	public LocalDate getDataConsulta() {
		return dataConsulta;
	}

	/**
	 * Define a data da consulta médica.
	 * 
	 * @param dataConsulta a nova data da consulta.
	 */
	public void setDataConsulta(LocalDate dataConsulta) {
		this.dataConsulta = dataConsulta;
	}

	/**
	 * Obtém o horário da consulta médica.
	 * 
	 * @return o horário do agendamento.
	 */
	public Horario getHorario() {
		return horario;
	}

	/**
	 * Define o horário da consulta médica.
	 * 
	 * @param horario o horário a ser associado ao agendamento.
	 */
	public void setHorario(Horario horario) {
		this.horario = horario;
	}
}