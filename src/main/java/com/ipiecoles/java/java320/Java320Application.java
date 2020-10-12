package com.ipiecoles.java.java320;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.EncodedResourceResolver;

@SpringBootApplication
public class Java320Application {

	public static void main(String[] args) {
		SpringApplication.run(Java320Application.class, args);
	}

}
