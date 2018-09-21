package com.cts.books.catalog.service.swagger;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.cts.books.catalog.service.controller"))
				.paths(PathSelectors.regex("/api.*")).build().apiInfo(this.metaData());
	}
	
	private ApiInfo metaData() {
		final ApiInfo apiInfo = new ApiInfo("Books Catalog API",
				"Spring Boot REST API for querying books", "1.0", "Terms of service",
				new Contact("Tenzin Dawa", "https://tendawa.wordpress.com", "tnzngdw@gmail.com"),
				"Apache License Version 2.0", "https://www.apache.org/licenses/LICENSE-2.0",
				new ArrayList<VendorExtension>());
		return apiInfo;
	}
}
