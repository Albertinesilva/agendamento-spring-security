package com.albertsilva.projects.consultamedica.security.model.entities;

import com.albertsilva.projects.consultamedica.model.entities.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Representa um perfil de usuário no sistema.
 * Cada perfil possui uma descrição única que define o tipo de acesso ou
 * permissões do usuário.
 */
@Entity
@Table(name = "perfis")
public class Perfil extends AbstractEntity {

	/**
	 * Descrição do perfil.
	 * A descrição é única e não pode ser nula.
	 */
	@Column(name = "descricao", nullable = false, unique = true)
	private String desc;

	/**
	 * Construtor padrão.
	 * Chama o construtor da classe pai {@link AbstractEntity}.
	 */
	public Perfil() {
		super();
	}

	/**
	 * Construtor que recebe o ID do perfil.
	 * 
	 * @param id o ID do perfil a ser definido.
	 */
	public Perfil(Long id) {
		super.setId(id);
	}

	/**
	 * Obtém a descrição do perfil.
	 * 
	 * @return a descrição do perfil.
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * Define a descrição do perfil.
	 * 
	 * @param desc a descrição a ser definida.
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
}
