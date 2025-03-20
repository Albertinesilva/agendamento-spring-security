package com.agendamedico.spring_security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.agendamedico.spring_security.service.EmailService;

@SpringBootApplication
public class SpringSecurityApplication implements CommandLineRunner {

	public static void main(String[] args) {
		// System.out.println(new BCryptPasswordEncoder().encode("123456"));
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

	@Autowired
	EmailService emailService;

	@Override
	public void run(String... args) throws Exception {
		emailService.enviarPedidoDeConfirmacaoDeCadastro("no.reply.smttsaj@gmail.com", "998877");
	}

}
