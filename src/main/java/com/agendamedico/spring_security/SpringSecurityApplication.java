package com.agendamedico.spring_security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringSecurityApplication {

	public static void main(String[] args) {
		// System.out.println(new BCryptPasswordEncoder().encode("123456"));
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

}
