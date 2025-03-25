package com.albertsilva.projects.consultamedica.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.albertsilva.projects.consultamedica.security.model.entities.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

/**
 * Representa um médico no sistema.
 * Contém informações sobre o médico, incluindo seu nome, CRM, data de
 * inscrição,
 * especialidades, agendamentos e o usuário associado.
 */
@Entity
@Table(name = "medicos")
public class Medico extends AbstractEntity {

	/**
	 * Nome do médico.
	 * O nome é único e não pode ser nulo.
	 */
	@Column(name = "nome", unique = true, nullable = false)
	private String nome;

	/**
	 * CRM (Conselho Regional de Medicina) do médico.
	 * O CRM é único e não pode ser nulo.
	 */
	@Column(name = "crm", unique = true, nullable = false)
	private Integer crm;

	/**
	 * Data de inscrição do médico no CRM.
	 * A data de inscrição não pode ser nula.
	 */
	@DateTimeFormat(iso = ISO.DATE)
	@Column(name = "data_inscricao", nullable = false)
	private LocalDate dtInscricao;

	/**
	 * Especialidades do médico.
	 * A relação é de muitos-para-muitos entre médicos e especialidades.
	 * A recursividade é evitada ao usar {@link JsonIgnore}.
	 */
	@JsonIgnore
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "medicos_tem_especialidades", 
	joinColumns = @JoinColumn(name = "id_medico", referencedColumnName = "id"), 
	inverseJoinColumns = @JoinColumn(name = "id_especialidade", referencedColumnName = "id"))
	private Set<Especialidade> especialidades;

	/**
	 * Agendamentos associados ao médico.
	 * A relação é de um-para-muitos entre médicos e agendamentos.
	 * A recursividade é evitada ao usar {@link JsonIgnore}.
	 */
	@JsonIgnore
	@OneToMany(mappedBy = "medico")
	private List<Agendamento> agendamentos = new ArrayList<>();

	/**
	 * Usuário associado ao médico.
	 * Quando o médico é removido, o usuário também será removido devido ao
	 * CascadeType.REMOVE.
	 */
	@OneToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "id_usuario")
	private Usuario usuario;

	/**
	 * Construtor padrão.
	 */
	public Medico() {
		super();
	}

	/**
	 * Construtor com ID.
	 * 
	 * @param id ID do médico.
	 */
	public Medico(Long id) {
		super.setId(id);
	}

	/**
	 * Construtor com usuário associado.
	 * 
	 * @param usuario Usuário associado ao médico.
	 */
	public Medico(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * Obtém o nome do médico.
	 * 
	 * @return o nome do médico.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Define o nome do médico.
	 * 
	 * @param nome o nome a ser definido.
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Obtém o CRM do médico.
	 * 
	 * @return o CRM do médico.
	 */
	public Integer getCrm() {
		return crm;
	}

	/**
	 * Define o CRM do médico.
	 * 
	 * @param crm o CRM a ser definido.
	 */
	public void setCrm(Integer crm) {
		this.crm = crm;
	}

	/**
	 * Obtém a data de inscrição do médico no CRM.
	 * 
	 * @return a data de inscrição.
	 */
	public LocalDate getDtInscricao() {
		return dtInscricao;
	}

	/**
	 * Define a data de inscrição do médico no CRM.
	 * 
	 * @param dtInscricao a data de inscrição a ser definida.
	 */
	public void setDtInscricao(LocalDate dtInscricao) {
		this.dtInscricao = dtInscricao;
	}

	/**
	 * Obtém as especialidades do médico.
	 * 
	 * @return as especialidades do médico.
	 */
	public Set<Especialidade> getEspecialidades() {
		return especialidades;
	}

	/**
	 * Define as especialidades do médico.
	 * 
	 * @param especialidades as especialidades a serem definidas.
	 */
	public void setEspecialidades(Set<Especialidade> especialidades) {
		this.especialidades = especialidades;
	}

	/**
	 * Obtém os agendamentos associados ao médico.
	 * 
	 * @return os agendamentos do médico.
	 */
	public List<Agendamento> getAgendamentos() {
		return agendamentos;
	}

	/**
	 * Define os agendamentos associados ao médico.
	 * 
	 * @param agendamentos os agendamentos a serem definidos.
	 */
	public void setAgendamentos(List<Agendamento> agendamentos) {
		this.agendamentos = agendamentos;
	}

	/**
	 * Obtém o usuário associado ao médico.
	 * 
	 * @return o usuário associado ao médico.
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * Define o usuário associado ao médico.
	 * 
	 * @param usuario o usuário a ser associado ao médico.
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
