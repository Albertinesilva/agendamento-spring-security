package com.albertsilva.projects.consultamedica.security.model.entities;

import java.util.ArrayList;
import java.util.List;

import com.albertsilva.projects.consultamedica.model.entities.AbstractEntity;
import com.albertsilva.projects.consultamedica.security.model.enums.PerfilTipo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;

/**
 * Classe que representa um usuário do sistema.
 * Contém informações como email, senha, perfis, e status de atividade do
 * usuário.
 */
@Entity
@Table(name = "usuarios", indexes = { @Index(name = "idx_usuario_email", columnList = "email") })
public class Usuario extends AbstractEntity {

	/** Email único do usuário. */
	@Column(name = "email", unique = true, nullable = false)
	private String email;

	/** Senha do usuário (não exposta em JSON). */
	@JsonIgnore
	@Column(name = "senha", nullable = false)
	private String senha;

	/** Perfis associados ao usuário. */
	@ManyToMany
	@JoinTable(name = "usuarios_tem_perfis", joinColumns = {
			@JoinColumn(name = "usuario_id", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "perfil_id", referencedColumnName = "id") })
	private List<Perfil> perfis = new ArrayList<>();

	/** Indica se o usuário está ativo ou não. */
	@Column(name = "ativo", nullable = false)
	private boolean ativo;

	/** Código verificador de 6 caracteres para o usuário. */
	@Column(name = "codigo_verificador", length = 6)
	private String codigoVerificador;

	/**
	 * Construtor padrão.
	 */
	public Usuario() {
		super();
	}

	/**
	 * Construtor que inicializa o usuário com um id.
	 * 
	 * @param id ID do usuário.
	 */
	public Usuario(Long id) {
		super.setId(id);
	}

	/**
	 * Construtor que inicializa o usuário com um email.
	 * 
	 * @param email Email do usuário.
	 */
	public Usuario(String email) {
		this.email = email;
	}

	/**
	 * Adiciona um perfil ao usuário baseado no tipo de perfil.
	 * 
	 * @param tipo Tipo do perfil a ser adicionado.
	 */
	public void addPerfil(PerfilTipo tipo) {
		if (this.perfis == null) {
			this.perfis = new ArrayList<>();
		}
		this.perfis.add(new Perfil(tipo.getCod()));
	}

	/**
	 * Obtém o email do usuário.
	 * 
	 * @return o email do usuário.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Define o email do usuário.
	 * 
	 * @param email o email do usuário.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Obtém a senha do usuário.
	 * 
	 * @return a senha do usuário.
	 */
	public String getSenha() {
		return senha;
	}

	/**
	 * Define a senha do usuário.
	 * 
	 * @param senha a senha do usuário.
	 */
	public void setSenha(String senha) {
		this.senha = senha;
	}

	/**
	 * Obtém a lista de perfis do usuário.
	 * 
	 * @return a lista de perfis do usuário.
	 */
	public List<Perfil> getPerfis() {
		return perfis;
	}

	/**
	 * Define a lista de perfis do usuário.
	 * 
	 * @param perfis lista de perfis do usuário.
	 */
	public void setPerfis(List<Perfil> perfis) {
		this.perfis = perfis;
	}

	/**
	 * Verifica se o usuário está ativo.
	 * 
	 * @return true se o usuário estiver ativo, false caso contrário.
	 */
	public boolean isAtivo() {
		return ativo;
	}

	/**
	 * Define o status de atividade do usuário.
	 * 
	 * @param ativo true se o usuário deve ser ativo, false caso contrário.
	 */
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	/**
	 * Obtém o código verificador do usuário.
	 * 
	 * @return o código verificador do usuário.
	 */
	public String getCodigoVerificador() {
		return codigoVerificador;
	}

	/**
	 * Define o código verificador do usuário.
	 * 
	 * @param codigoVerificador o código verificador do usuário.
	 */
	public void setCodigoVerificador(String codigoVerificador) {
		this.codigoVerificador = codigoVerificador;
	}
}
