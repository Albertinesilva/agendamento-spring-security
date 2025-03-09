package com.agendamedico.spring_security.web.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionController {

  // tratamento de exceção de usuário não encontrado
  @ExceptionHandler(UsernameNotFoundException.class)
  public ModelAndView usuarioNaoEncontradoException(UsernameNotFoundException ex) {
    ModelAndView mv = new ModelAndView("error");
    mv.addObject("status", 404);
    mv.addObject("error", "Operação não pode ser realizada");
    mv.addObject("message", ex.getMessage());
    return mv;
  }
}