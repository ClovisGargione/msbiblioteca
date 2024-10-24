package io.github.clovisgargione.msautenticacao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenAPI() {
	    return new OpenAPI()
	        .info(new Info()
	            .title("Aplicação para autenticação de API")
	            .description("Este é o authorization server.")
	            .termsOfService("terms")
	            .contact(new Contact().email("clovis.gargione@gmail.com").name("Developer: Clovis Gargione"))
	            .license(new License().name("v1.0.0"))
	            .version("1.0")
	        );
	}
}
