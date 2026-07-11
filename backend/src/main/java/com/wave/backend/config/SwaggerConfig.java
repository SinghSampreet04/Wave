package com.wave.backend.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI waveOpenAPI() {

        return new OpenAPI()

                .info(
                        new Info()

                                .title("Wave API")

                                .description("""
                                        Wave is a real-time collaboration platform
                                        built with Spring Boot, JWT Authentication,
                                        PostgreSQL and WebSockets.
                                        """)

                                .version("1.0.0")

                                .contact(
                                        new Contact()
                                                .name("Sam")
                                                .email("sam@example.com")
                                )

                                .license(
                                        new License()
                                                .name("MIT License")
                                )
                )

                .externalDocs(
                        new ExternalDocumentation()
                                .description("Wave Documentation")
                                .url("https://github.com/")
                );

    }

}