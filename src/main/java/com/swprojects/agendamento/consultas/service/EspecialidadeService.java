package com.swprojects.agendamento.consultas.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.swprojects.agendamento.consultas.datatables.Datatables;
import com.swprojects.agendamento.consultas.datatables.DatatablesColunas;
import com.swprojects.agendamento.consultas.domain.Especialidade;
import com.swprojects.agendamento.consultas.repository.EspecialidadeRepository;

@Service
public class EspecialidadeService {

	@Autowired
	private EspecialidadeRepository repository;
	@Autowired
	private Datatables datatables;

	@Transactional(readOnly = false)
	public void salvar(Especialidade especialidade) {
		
		repository.save(especialidade);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarEspecialidades(HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.ESPECIALIDADES);
		Page<?> page = datatables.getSearch().isEmpty()
				? repository.findAll(datatables.getPageable())
				: repository.findAllByTitulo(datatables.getSearch(), datatables.getPageable());
		return datatables.getResponse(page);
	}

	@Transactional(readOnly = true)
	public Especialidade buscarPorId(Long id) {
		
		return repository.findById(id).get();
	}

	@Transactional(readOnly = false)
	public void remover(Long id) {
		
		repository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<String> buscarEspecialidadeByTermo(String termo) {
		
		return repository.findEspecialidadesByTermo(termo);
	}

	@Transactional(readOnly = true)
	public Set<Especialidade> buscarPorTitulos(String[] titulos) {
		
		return repository.findByTitulos(titulos);
	}

	@Transactional(readOnly = true)
	public Map<String, Object> buscarEspecialidadesPorMedico(Long id, HttpServletRequest request) {
		datatables.setRequest(request);
		datatables.setColunas(DatatablesColunas.ESPECIALIDADES);
		Page<Especialidade> page = repository.findByIdMedico(id, datatables.getPageable()); 
		return datatables.getResponse(page);
	}
	
	
}
