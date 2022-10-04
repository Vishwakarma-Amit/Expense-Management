package com.expense.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

	@Bean
	public Docket swaggerConfiguration() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("com.expense"))
				.paths(PathSelectors.any()).build().apiInfo(apiDetails());
	}

	private ApiInfo apiDetails() {
		return new ApiInfo("Expense API", "Expense-Service", "1.0", "@Copyright Application",
				new springfox.documentation.service.Contact("Amit Vishwakarma",
						"https://www.linkedin.com/in/amit-vishwakarma-74753a149/", "vishwakarma.amit@outlook.in"),
				"API Licenses", "localhost:8080", Collections.emptyList());
	}

}
