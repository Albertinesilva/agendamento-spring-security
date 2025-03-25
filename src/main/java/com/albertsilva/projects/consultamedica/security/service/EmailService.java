package com.albertsilva.projects.consultamedica.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

  @Autowired
  private JavaMailSender mailSender;

  @Autowired
  private SpringTemplateEngine templateEngine;

  public void enviarPedidoDeConfirmacaoDeCadastro(String destinatario, String codigo) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

    Context context = new Context();
    context.setVariable("titulo", "Bem-vindo a clínica Spring Security");
    context.setVariable("texto", "Precisamos que confirme seu cadastro, clicando no link abaixo");
    context.setVariable("linkConfirmacao", "https://clinica-spring-security-b074f1332545.herokuapp.com/u/confirmacao/cadastro?codigo=" + codigo);

    String html = templateEngine.process("email/confirmacao", context);
    helper.setTo(destinatario);
    helper.setText(html, true);
    helper.setSubject("Confirmação de cadastro");
    helper.setFrom("nao-responder@clinica.com.br");

    helper.addInline("logo", new ClassPathResource("/static/image/spring-security.png"));

    mailSender.send(message);
  }

  public void enviarPedidoDeRedefinicaoSenha(String destinatario, String verificador) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

    Context context = new Context();
    context.setVariable("titulo", "Redefinição de Senha");
    context.setVariable("texto", "Para redefinir sua senha use o código de verificação quando exigido no formulário");
    context.setVariable("verificador", verificador);

    String html = templateEngine.process("email/confirmacao", context);
    helper.setTo(destinatario);
    helper.setText(html, true);
    helper.setSubject("Redefinição de Senha");
    helper.setFrom("nao-responder@clinica.com.br");

    helper.addInline("logo", new ClassPathResource("/static/image/spring-security.png"));

    mailSender.send(message);
  }
}
