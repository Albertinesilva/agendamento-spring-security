package com.agendamedico.spring_security.datatables;

/**
 * Classe utilitária que define os nomes das colunas utilizadas no DataTables
 * para diferentes entidades do sistema.
 */
public class DatatablesColunas {

	/**
	 * Colunas utilizadas para a listagem de especialidades médicas.
	 */
	public static final String[] ESPECIALIDADES = { "id", "titulo" };

	/**
	 * Colunas utilizadas para a listagem de médicos.
	 */
	public static final String[] MEDICOS = { "id", "nome", "crm", "dtInscricao", "especialidades" };

	/**
	 * Colunas utilizadas para a listagem de agendamentos médicos.
	 */
	public static final String[] AGENDAMENTOS = { "id", "paciente.nome", "dataConsulta", "medico.nome",
			"especialidade.titulo" };

	/**
	 * Colunas utilizadas para a listagem de usuários do sistema.
	 */
	public static final String[] USUARIOS = { "id", "email", "ativo", "perfis" };
}
