package com.agendamedico.spring_security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/*https://clinica-spring-security-b074f1332545.herokuapp.com/login */
public class SpringSecurityApplication {

	public static void main(String[] args) {
		// System.out.println(new BCryptPasswordEncoder().encode(""));
		SpringApplication.run(SpringSecurityApplication.class, args);
	}
}
