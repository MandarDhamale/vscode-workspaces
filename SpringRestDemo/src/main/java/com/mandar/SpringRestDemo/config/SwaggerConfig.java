package com.mandar.SpringRestDemo.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Demo API",
        version = "Version 1.0",
        contact = @Contact(
            name = "mandar", email="mandardhamale@gmail.com", url = "https://mandar.com"
        ),
        license = @License(
            name = "Apache 2.0", url = "https://www.apache.org/licenses/"
        ),
        termsOfService = "https://mandar.com",
        description = "Spring Boot RestFul API demo"
    )
)



public class SwaggerConfig {
    
}