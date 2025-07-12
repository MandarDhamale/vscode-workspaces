package com.mandar.SpringRestApi.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(title = "User API", version = "Verstion 1.0", contact = @Contact(name = "Mandar Dhamale", email = "mandardhamale@gmail.com", url = "mandardhamale.com "), description = "Spring Boot RESTful Albums API"))
public class SwaggerConfig {

}
