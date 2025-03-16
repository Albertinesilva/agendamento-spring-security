package com.agendamedico.spring_security.web.conversor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.agendamedico.spring_security.domain.Especialidade;
import com.agendamedico.spring_security.service.EspecialidadeService;

import org.springframework.lang.NonNull;

@Component
public class EspecialidadesConverter implements Converter<String[], Set<Especialidade>> {

  @Autowired
  private EspecialidadeService especialidadeService;

  @Override
  public Set<Especialidade> convert(@NonNull String[] titulos) {

    Set<Especialidade> especialidades = new HashSet<>();
    if (titulos != null && titulos.length > 0) {
      especialidades.addAll(especialidadeService.buscarPorTitulos(titulos));
    }
    return especialidades;
  }

}
