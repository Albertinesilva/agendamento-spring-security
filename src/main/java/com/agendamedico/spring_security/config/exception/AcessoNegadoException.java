package com.agendamedico.spring_security.config.exception;

public class AcessoNegadoException extends RuntimeException {

  public AcessoNegadoException(String message) {
    super(message);
  }
}
