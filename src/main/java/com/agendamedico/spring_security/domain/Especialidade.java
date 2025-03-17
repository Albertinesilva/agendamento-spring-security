package com.agendamedico.spring_security.domain;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

/**
 * Representa uma especialidade médica no sistema.
 * Cada especialidade pode estar associada a vários médicos.
 */
@Entity
@Table(name = "especialidades", indexes = { @Index(name = "idx_especialidade_titulo", columnList = "titulo") })
public class Especialidade extends AbstractEntity {

	/**
	 * Título da especialidade médica.
	 * Deve ser único e não pode ser nulo.
	 */
	@Column(name = "titulo", unique = true, nullable = false)
	private String titulo;

	/**
	 * Descrição detalhada da especialidade médica.
	 */
	@Column(name = "descricao", columnDefinition = "TEXT")
	private String descricao;

	/**
	 * Lista de médicos associados a esta especialidade.
	 * Representa um relacionamento muitos-para-muitos entre especialidades e
	 * médicos.
	 */
	@ManyToMany
	@JoinTable(name = "medicos_tem_especialidades", 
	joinColumns = @JoinColumn(name = "id_especialidade", referencedColumnName = "id"), 
	inverseJoinColumns = @JoinColumn(name = "id_medico", referencedColumnName = "id"))
	private List<Medico> medicos;

	/**
	 * Obtém o título da especialidade.
	 * 
	 * @return o título da especialidade.
	 */
	public String getTitulo() {
		return titulo;
	}

	/**
	 * Define o título da especialidade.
	 * 
	 * @param titulo o título a ser definido.
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
	 * Obtém a descrição da especialidade.
	 * 
	 * @return a descrição da especialidade.
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * Define a descrição da especialidade.
	 * 
	 * @param descricao a descrição a ser definida.
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	/**
	 * Obtém a lista de médicos associados a esta especialidade.
	 * 
	 * @return a lista de médicos.
	 */
	public List<Medico> getMedicos() {
		return medicos;
	}

	/**
	 * Define a lista de médicos associados a esta especialidade.
	 * 
	 * @param medicos a lista de médicos a ser associada.
	 */
	public void setMedico(List<Medico> medicos) {
		this.medicos = medicos;
	}
}
