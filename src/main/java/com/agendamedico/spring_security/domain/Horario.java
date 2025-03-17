package com.agendamedico.spring_security.domain;

import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

/**
 * Representa um horário disponível para agendamento.
 * Cada horário é identificado por um valor único de hora e minuto.
 */
@Entity
@Table(name = "horas", indexes = { @Index(name = "idx_hora_minuto", columnList = "hora_minuto") })
public class Horario extends AbstractEntity {

	/**
	 * Representa a hora e o minuto do horário.
	 * O valor é único e não pode ser nulo.
	 */
	@Column(name = "hora_minuto", unique = true, nullable = false)
	private LocalTime horaMinuto;

	/**
	 * Obtém o horário (hora e minuto).
	 * 
	 * @return o horário como um objeto {@link LocalTime}.
	 */
	public LocalTime getHoraMinuto() {
		return horaMinuto;
	}

	/**
	 * Define o horário (hora e minuto).
	 * 
	 * @param horaMinuto o horário a ser definido, como um objeto {@link LocalTime}.
	 */
	public void setHoraMinuto(LocalTime horaMinuto) {
		this.horaMinuto = horaMinuto;
	}
}
