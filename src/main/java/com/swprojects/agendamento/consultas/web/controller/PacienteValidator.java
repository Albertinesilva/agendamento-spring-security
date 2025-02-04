package com.swprojects.agendamento.consultas.web.controller;

import com.swprojects.agendamento.consultas.domain.Paciente;
import com.swprojects.agendamento.consultas.service.PacienteService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PacienteValidator implements Validator {

    PacienteService service;

    public PacienteValidator(PacienteService service) {
        this.service = service;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return Paciente.class.equals(clazz);

    }

    @Override
    public void validate(Object object, Errors errors) {

        Paciente entity = (Paciente) object;

        if (entity.getId() != null) {

            if (1 != 0) {

                errors.rejectValue("nome", "Existe.nome");

            }

        }

    }
}
