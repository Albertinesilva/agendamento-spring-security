package com.albertsilva.projects.consultamedica;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/* https://clinica-spring-security-b074f1332545.herokuapp.com/login */
public class ConsultamedicaApplication {

	public static void main(String[] args) {
		// System.out.println(new BCryptPasswordEncoder().encode(""));
		SpringApplication.run(ConsultamedicaApplication.class, args);
	}

}
