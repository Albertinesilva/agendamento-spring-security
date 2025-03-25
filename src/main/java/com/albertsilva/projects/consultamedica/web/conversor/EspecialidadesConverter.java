package com.albertsilva.projects.consultamedica.web.conversor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.albertsilva.projects.consultamedica.model.entities.Especialidade;
import com.albertsilva.projects.consultamedica.services.EspecialidadeService;

import org.springframework.lang.NonNull;

/**
 * Conversor responsável por converter um array de strings (títulos) para um
 * conjunto de especialidades.
 * Utiliza o serviço {@link EspecialidadeService} para buscar as especialidades
 * com base nos títulos fornecidos.
 * 
 * @see EspecialidadeService
 */
@Component
public class EspecialidadesConverter implements Converter<String[], Set<Especialidade>> {

  @Autowired
  private EspecialidadeService especialidadeService;

  /**
   * Converte um array de strings contendo os títulos das especialidades em um
   * conjunto de especialidades.
   * 
   * @param titulos Um array de strings contendo os títulos das especialidades.
   * @return Um conjunto de {@link Especialidade} correspondentes aos títulos
   *         fornecidos.
   */
  @Override
  public Set<Especialidade> convert(@NonNull String[] titulos) {

    Set<Especialidade> especialidades = new HashSet<>();
    if (titulos != null && titulos.length > 0) {
      // Busca as especialidades com base nos títulos fornecidos
      especialidades.addAll(especialidadeService.buscarPorTitulos(titulos));
    }
    return especialidades;
  }
}
