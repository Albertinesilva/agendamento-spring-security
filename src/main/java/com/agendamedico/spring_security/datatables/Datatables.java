package com.agendamedico.spring_security.datatables;

import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * Classe utilitária para manipulação de requisições do DataTables no backend.
 * Essa classe processa os parâmetros da requisição e retorna os dados no
 * formato esperado pelo DataTables.
 */
@Component
public class Datatables {

	private HttpServletRequest request;
	private String[] colunas;

	/**
	 * Construtor padrão.
	 */
	public Datatables() {
		super();
	}

	/**
	 * Constrói a resposta JSON esperada pelo DataTables a partir de uma página de
	 * dados.
	 *
	 * @param page Página de dados obtida da consulta.
	 * @return Mapa contendo os dados formatados para o DataTables.
	 */
	public Map<String, Object> getResponse(Page<?> page) {
		Map<String, Object> json = new LinkedHashMap<>();
		json.put("draw", draw());
		json.put("recordsTotal", page.getTotalElements());
		json.put("recordsFiltered", page.getTotalElements());
		json.put("data", page.getContent());
		return json;
	}

	/**
	 * Obtém a requisição HTTP associada.
	 *
	 * @return Objeto {@link HttpServletRequest}.
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * Define a requisição HTTP associada.
	 *
	 * @param request Objeto {@link HttpServletRequest}.
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Obtém a lista de colunas utilizadas na tabela.
	 *
	 * @return Array de strings com os nomes das colunas.
	 */
	public String[] getColunas() {
		return colunas;
	}

	/**
	 * Define a lista de colunas utilizadas na tabela.
	 *
	 * @param colunas Array de strings com os nomes das colunas.
	 */
	public void setColunas(String[] colunas) {
		this.colunas = colunas;
	}

	/**
	 * Obtém o número do draw (requisição atual do DataTables).
	 *
	 * @return Número do draw.
	 */
	private int draw() {
		return Integer.parseInt(this.request.getParameter("draw"));
	}

	/**
	 * Obtém o índice inicial dos registros da página.
	 *
	 * @return Índice de início dos registros.
	 */
	private int start() {
		return Integer.parseInt(this.request.getParameter("start"));
	}

	/**
	 * Obtém o número de registros por página.
	 *
	 * @return Quantidade de registros por página.
	 */
	public int getLength() {
		return Integer.parseInt(this.request.getParameter("length"));
	}

	/**
	 * Calcula a página atual com base nos parâmetros da requisição.
	 *
	 * @return Número da página atual.
	 */
	public int getCurrentPage() {
		return start() / getLength();
	}

	/**
	 * Obtém o nome da coluna pela qual a ordenação está sendo feita.
	 *
	 * @return Nome da coluna utilizada na ordenação.
	 */
	public String getColumnName() {
		int iCol = Integer.parseInt(this.request.getParameter("order[0][column]"));
		return this.colunas[iCol];
	}

	/**
	 * Obtém a direção da ordenação (ASC ou DESC).
	 *
	 * @return Direção da ordenação como {@link Sort.Direction}.
	 */
	public Sort.Direction getDirection() {
		String order = this.request.getParameter("order[0][dir]");
		return order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
	}

	/**
	 * Obtém o valor do campo de busca do DataTables.
	 *
	 * @return String com o termo de busca.
	 */
	public String getSearch() {
		return this.request.getParameter("search[value]");
	}

	/**
	 * Cria um objeto {@link Pageable} com as configurações de paginação e
	 * ordenação.
	 *
	 * @return Objeto {@link Pageable} contendo os parâmetros da requisição.
	 */
	public Pageable getPageable() {
		return PageRequest.of(getCurrentPage(), getLength(), getDirection(), getColumnName());
	}
}
