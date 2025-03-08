package com.agendamedico.spring_security.web.conversor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.agendamedico.spring_security.domain.Perfil;

@Component
public class PerfisConverter implements Converter<String[], List<Perfil>> {

  @Override
  public List<Perfil> convert(@NonNull String[] source) {
    List<Perfil> perfis = new ArrayList<>();
    for (String id : source) {
      if (!id.equals("0")) {
        perfis.add(new Perfil(Long.parseLong(id)));
      }
    }
    return perfis;
  }

}
