package com.agendamedico.spring_security.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agendamedico.spring_security.datatables.Datatables;
import com.agendamedico.spring_security.datatables.DatatablesColunas;
import com.agendamedico.spring_security.domain.Especialidade;
import com.agendamedico.spring_security.repository.EspecialidadeRepository;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class EspecialidadeService {

  @Autowired
  private EspecialidadeRepository especialidadeRepository;

  @Autowired
  private Datatables dataTables;

  @Transactional(readOnly = false)
  public void salvar(Especialidade especialidade) {
    especialidadeRepository.save(especialidade);
  }

  @Transactional(readOnly = true)
  public Map<String, Object> buscarEspecialidades(HttpServletRequest request) {
    dataTables.setRequest(request);
    dataTables.setColunas(DatatablesColunas.ESPECIALIDADES);
    Page<?> page = dataTables.getSearch().isEmpty()
        ? especialidadeRepository.findAll(dataTables.getPageable())
        : especialidadeRepository.findAllByTitulo(dataTables.getSearch(), dataTables.getPageable());
    return dataTables.getResponse(page);
  }

  @Transactional(readOnly = true)
  public Especialidade buscarPorId(Long id) {
    return especialidadeRepository.findById(id).get();
  }

  @Transactional(readOnly = false)
  public void remover(Long id) {
    especialidadeRepository.deleteById(id);
  }

  @Transactional(readOnly = true)
  public List<String> buscarEspecialidadeByTermo(String termo) {
    return especialidadeRepository.findEspecialidadesByTermo(termo);
  }

  @Transactional(readOnly = true)
  public Set<Especialidade> buscarPorTitulos(String[] titulos) {
    return especialidadeRepository.findByTitulos(titulos);
  }

  @Transactional(readOnly = true)
  public Map<String, Object> buscarEspecialidadesPorMedico(Long id, HttpServletRequest request) {
    dataTables.setRequest(request);
    dataTables.setColunas(DatatablesColunas.ESPECIALIDADES);
    Page<Especialidade> page = especialidadeRepository.findByIdMedico(id, dataTables.getPageable());
    return dataTables.getResponse(page);
  }

}
