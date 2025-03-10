package br.edu.ifpb.hopin_daw2.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ropin")
                        .description("O Hopin é um projeto desenvolvido para fins de estudo do Spring Boot, no contexto da disciplina de DAWII, com o objetivo de criar uma API REST que replique funcionalidades básicas do Uber.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Código no github")
                                .url("https://github.com/lucasramallo/hopin-daw2"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0")));
    }
}
