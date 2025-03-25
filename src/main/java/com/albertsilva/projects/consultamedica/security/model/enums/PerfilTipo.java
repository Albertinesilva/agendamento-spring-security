package com.albertsilva.projects.consultamedica.security.model.enums;

/**
 * Enum que representa os tipos de perfil de usuário no sistema.
 * Cada tipo de perfil tem um código único e uma descrição.
 */
public enum PerfilTipo {

	/** Representa o perfil de administrador. */
	ADMIN(1, "ADMIN"),

	/** Representa o perfil de médico. */
	MEDICO(2, "MEDICO"),

	/** Representa o perfil de paciente. */
	PACIENTE(3, "PACIENTE");

	/** Código único associado ao perfil. */
	private long cod;

	/** Descrição do perfil. */
	private String desc;

	/**
	 * Construtor privado para inicializar os valores do tipo de perfil.
	 * 
	 * @param cod  código único associado ao perfil.
	 * @param desc descrição do perfil.
	 */
	private PerfilTipo(long cod, String desc) {
		this.cod = cod;
		this.desc = desc;
	}

	/**
	 * Obtém o código do perfil.
	 * 
	 * @return o código associado ao perfil.
	 */
	public long getCod() {
		return cod;
	}

	/**
	 * Obtém a descrição do perfil.
	 * 
	 * @return a descrição do perfil.
	 */
	public String getDesc() {
		return desc;
	}
}
