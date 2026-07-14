package com.wave.backend.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI waveOpenAPI() {

        final String securitySchemeName = "Bearer Authentication";

        return new OpenAPI()

                .info(
                        new Info()

                                .title("Wave API")

                                .description("""
                                        Wave is a real-time collaboration platform
                                        built with Spring Boot, JWT Authentication,
                                        PostgreSQL, Redis and WebSockets.
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
                )

                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchemeName)
                )

                .components(
                        new Components()
                                .addSecuritySchemes(
                                        securitySchemeName,
                                        new SecurityScheme()
                                                .name("Authorization")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );

    }

}