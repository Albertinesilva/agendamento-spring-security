package com.agendamedico.spring_security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa um paciente no sistema.
 * Contém informações sobre o paciente, incluindo seu nome, data de nascimento,
 * agendamentos e o usuário associado.
 */
@Entity
@Table(name = "pacientes")
public class Paciente extends AbstractEntity {

	/**
	 * Nome do paciente.
	 * O nome é único e não pode ser nulo.
	 */
	@Column(name = "nome", unique = true, nullable = false)
	private String nome;

	/**
	 * Data de nascimento do paciente.
	 * A data de nascimento não pode ser nula.
	 */
	@Column(name = "data_nascimento", nullable = false)
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate dtNascimento;

	/**
	 * Agendamentos associados ao paciente.
	 * A relação é de um-para-muitos entre pacientes e agendamentos.
	 * A recursividade é evitada ao usar {@link JsonIgnore}.
	 */
	@JsonIgnore
	@OneToMany(mappedBy = "paciente")
	private List<Agendamento> agendamentos = new ArrayList<>();

	/**
	 * Usuário associado ao paciente.
	 * Quando o paciente é removido, o usuário também será removido devido ao
	 * CascadeType.REMOVE.
	 */
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	public Paciente() {

	}

	public Paciente(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * Obtém o nome do paciente.
	 * 
	 * @return o nome do paciente.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Define o nome do paciente.
	 * 
	 * @param nome o nome a ser definido.
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Obtém a data de nascimento do paciente.
	 * 
	 * @return a data de nascimento.
	 */
	public LocalDate getDtNascimento() {
		return dtNascimento;
	}

	/**
	 * Define a data de nascimento do paciente.
	 * 
	 * @param dtNascimento a data de nascimento a ser definida.
	 */
	public void setDtNascimento(LocalDate dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	/**
	 * Obtém os agendamentos associados ao paciente.
	 * 
	 * @return os agendamentos do paciente.
	 */
	public List<Agendamento> getAgendamentos() {
		return agendamentos;
	}

	/**
	 * Define os agendamentos associados ao paciente.
	 * 
	 * @param agendamentos os agendamentos a serem definidos.
	 */
	public void setAgendamentos(List<Agendamento> agendamentos) {
		this.agendamentos = agendamentos;
	}

	/**
	 * Obtém o usuário associado ao paciente.
	 * 
	 * @return o usuário associado ao paciente.
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * Define o usuário associado ao paciente.
	 * 
	 * @param usuario o usuário a ser associado ao paciente.
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
