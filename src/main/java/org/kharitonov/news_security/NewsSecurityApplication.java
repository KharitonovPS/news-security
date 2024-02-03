package org.kharitonov.news_security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@SpringBootApplication
public class NewsSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(NewsSecurityApplication.class, args);
	}
}
