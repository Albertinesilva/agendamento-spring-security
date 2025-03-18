package com.agendamedico.spring_security.web.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.agendamedico.spring_security.config.exception.AcessoNegadoException;

/**
 * Classe de tratamento global de exceções na aplicação.
 * 
 * Esta classe captura exceções específicas e retorna a resposta apropriada.
 * No caso do {@link UsernameNotFoundException}, um erro 404 é retornado.
 */
@ControllerAdvice
public class ExceptionController {

  /**
   * Tratamento da exceção {@link UsernameNotFoundException}, que ocorre quando o
   * usuário não é encontrado.
   * 
   * @param ex A exceção {@link UsernameNotFoundException} que foi lançada.
   * @return Uma página de erro com status 404 e uma mensagem informando que a
   *         operação não pôde ser realizada.
   */
  @ExceptionHandler(UsernameNotFoundException.class)
  public ModelAndView usuarioNaoEncontradoException(UsernameNotFoundException ex) {
    // Criação da ModelAndView para exibir a página de erro
    ModelAndView mv = new ModelAndView("error");

    // Adicionando atributos à ModelAndView para exibição
    mv.addObject("status", 404);
    mv.addObject("error", "Operação não pode ser realizada");
    mv.addObject("message", ex.getMessage());

    return mv;
  }

  @ExceptionHandler(AcessoNegadoException.class)
  public ModelAndView acessoNegadoException(AcessoNegadoException ex) {
    // Criação da ModelAndView para exibir a página de erro
    ModelAndView mv = new ModelAndView("error");

    // Adicionando atributos à ModelAndView para exibição
    mv.addObject("status", 403);
    mv.addObject("error", "Operação não pode ser realizada");
    mv.addObject("message", ex.getMessage());

    return mv;
  }
}
