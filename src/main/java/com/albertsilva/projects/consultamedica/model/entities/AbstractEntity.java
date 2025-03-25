package com.albertsilva.projects.consultamedica.model.entities;

import java.io.Serializable;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Classe abstrata que serve como base para todas as entidades persistentes do
 * sistema.
 * <p>
 * Define um identificador único para cada entidade e implementa métodos
 * auxiliares
 * para facilitar o gerenciamento do ID.
 * </p>
 */
@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

	/**
	 * Identificador único da entidade, gerado automaticamente pelo banco de dados.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Construtor padrão. */
	public AbstractEntity() {
		super();
	}

	/**
	 * Obtém o identificador da entidade.
	 * 
	 * @return O ID da entidade.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Define o identificador da entidade.
	 * 
	 * @param id O novo ID da entidade.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Verifica se a entidade ainda não possui um ID atribuído.
	 * 
	 * @return {@code true} se o ID for {@code null}, caso contrário {@code false}.
	 */
	public boolean hasNotId() {
		return id == null;
	}

	/**
	 * Verifica se a entidade já possui um ID atribuído.
	 * 
	 * @return {@code true} se o ID não for {@code null}, caso contrário
	 *         {@code false}.
	 */
	public boolean hasId() {
		return id != null;
	}

	/**
	 * Calcula o código hash baseado no ID da entidade.
	 * 
	 * @return O valor do hash code da entidade.
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * Compara esta entidade com outra para verificar igualdade.
	 * 
	 * @param obj O objeto a ser comparado.
	 * @return {@code true} se os objetos forem iguais, caso contrário
	 *         {@code false}.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntity other = (AbstractEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/**
	 * Retorna uma representação textual da entidade.
	 * 
	 * @return Uma string representando a entidade e seu ID.
	 */
	@Override
	public String toString() {
		return String.format("Entidade %s id: %s", this.getClass().getName(), getId());
	}
}
