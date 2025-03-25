package com.albertsilva.projects.consultamedica.web.conversor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.albertsilva.projects.consultamedica.security.model.entities.Perfil;

/**
 * Conversor responsável por converter um array de strings (IDs de perfis) em
 * uma lista de objetos {@link Perfil}.
 * 
 * Este conversor é utilizado, por exemplo, para converter os IDs dos perfis
 * recebidos como parâmetros de uma requisição
 * em instâncias de {@link Perfil}.
 */
@Component
public class PerfisConverter implements Converter<String[], List<Perfil>> {

  /**
   * Converte um array de strings contendo os IDs dos perfis em uma lista de
   * objetos {@link Perfil}.
   * 
   * @param source Um array de strings representando os IDs dos perfis.
   * @return Uma lista de objetos {@link Perfil} correspondentes aos IDs
   *         fornecidos.
   */
  @Override
  public List<Perfil> convert(@NonNull String[] source) {
    List<Perfil> perfis = new ArrayList<>();
    for (String id : source) {
      // Ignora o ID "0", que pode ser utilizado como valor default
      if (!id.equals("0")) {
        perfis.add(new Perfil(Long.parseLong(id)));
      }
    }
    return perfis;
  }

}
