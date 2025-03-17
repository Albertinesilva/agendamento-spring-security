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

  /**
   * Salva uma especialidade no banco de dados.
   *
   * @param especialidade A especialidade a ser salva.
   */
  @Transactional(readOnly = false)
  public void salvar(Especialidade especialidade) {
    especialidadeRepository.save(especialidade);
  }

  /**
   * Busca especialidades com base nos filtros de pesquisa fornecidos na
   * requisição HTTP.
   *
   * @param request A requisição HTTP contendo os parâmetros de filtro e
   *                paginação.
   * @return Um mapa contendo os dados da página de especialidades.
   */
  @Transactional(readOnly = true)
  public Map<String, Object> buscarEspecialidades(HttpServletRequest request) {
    dataTables.setRequest(request);
    dataTables.setColunas(DatatablesColunas.ESPECIALIDADES);
    Page<?> page = dataTables.getSearch().isEmpty()
        ? especialidadeRepository.findAll(dataTables.getPageable())
        : especialidadeRepository.findAllByTitulo(dataTables.getSearch(), dataTables.getPageable());
    return dataTables.getResponse(page);
  }

  /**
   * Busca uma especialidade pelo seu ID.
   *
   * @param id O ID da especialidade a ser buscada.
   * @return A especialidade encontrada.
   * @throws RuntimeException Se a especialidade não for encontrada.
   */
  @Transactional(readOnly = true)
  public Especialidade buscarPorId(Long id) {
    return especialidadeRepository.findById(id).get();
  }

  /**
   * Remove uma especialidade pelo seu ID.
   *
   * @param id O ID da especialidade a ser removida.
   */
  @Transactional(readOnly = false)
  public void remover(Long id) {
    especialidadeRepository.deleteById(id);
  }

  /**
   * Busca especialidades com base em um termo de pesquisa.
   *
   * @param termo O termo de pesquisa a ser utilizado no filtro.
   * @return Uma lista de títulos de especialidades que correspondem ao termo de
   *         pesquisa.
   */
  @Transactional(readOnly = true)
  public List<String> buscarEspecialidadeByTermo(String termo) {
    return especialidadeRepository.findEspecialidadesByTermo(termo);
  }

  /**
   * Busca especialidades com base em um array de títulos.
   *
   * @param titulos Um array de títulos de especialidades.
   * @return Um conjunto de especialidades que correspondem aos títulos
   *         fornecidos.
   */
  @Transactional(readOnly = true)
  public Set<Especialidade> buscarPorTitulos(String[] titulos) {
    return especialidadeRepository.findByTitulos(titulos);
  }

  /**
   * Busca especialidades de um médico específico.
   *
   * @param id      O ID do médico.
   * @param request A requisição HTTP contendo os parâmetros de filtro e
   *                paginação.
   * @return Um mapa contendo os dados da página de especialidades do médico.
   */
  @Transactional(readOnly = true)
  public Map<String, Object> buscarEspecialidadesPorMedico(Long id, HttpServletRequest request) {
    dataTables.setRequest(request);
    dataTables.setColunas(DatatablesColunas.ESPECIALIDADES);
    Page<Especialidade> page = especialidadeRepository.findByIdMedico(id, dataTables.getPageable());
    return dataTables.getResponse(page);
  }

}
