package com.alex.cryptotracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Crypto Tracker API")
                        .version("1.0")
                        .description("API pentru monitorizarea preturilor crypto si trimiterea de alerte automate prin email.")
                        .contact(new Contact()
                                .name("Birlea Bogdan Alexandru")
                                .email("birlea24@gmail.com")));
    }
}