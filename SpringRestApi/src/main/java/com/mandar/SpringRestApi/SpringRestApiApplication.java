package com.mandar.SpringRestApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@SecurityScheme(name = "mrd-api", scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
@ConfigurationPropertiesScan
public class SpringRestApiApplication {

	//Tag: JWT & MySQL source setup for new projects
	public static void main(String[] args) {
		SpringApplication.run(SpringRestApiApplication.class, args);
		System.out.println("Application up and running...");
	}

}
