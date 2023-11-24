package com.blogapp.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
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
	public OpenAPI openAPI() {
		String schemeName = "bearerScheme";
		return new OpenAPI()
				.addSecurityItem(new SecurityRequirement()
						.addList(schemeName))
				.components(new Components()
						.addSecuritySchemes(schemeName, new SecurityScheme()
								.name(schemeName)
								.type(SecurityScheme.Type.HTTP)
								.bearerFormat("JWT")
								.scheme("Bearer")))
				.info(new Info()
						.title("Blog App API")
						.description("This is Blog App Project API")
						.version("1.0")
						.contact(new Contact().name("Kundan Kumar").email("blogapp234@gmail.com").url("http://localhost:4200"))
						.license(new License().name("Apache 2.0").url("http://springdoc.org")))
				.externalDocs(new ExternalDocumentation()
						.description("Blog App Project Documentation")
						.url("http://localhost:4200"));
	}

}
