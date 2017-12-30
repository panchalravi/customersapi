package com.ca.restdemo;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig extends WebMvcConfigurerAdapter {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.groupName("customer-api")
				.apiInfo(apiInfo())
				.select()
		          .apis(RequestHandlerSelectors.any())
		          //.paths(PathSelectors.any())
		          .paths(regex("/customers.*"))
		          .build()
		          //.pathMapping("/customers*")				
		        .useDefaultResponseMessages(false);
				
		        /*
		        .select()
				.apis(RequestHandlerSelectors.basePackage("com.ca.restdemo")).paths(PathSelectors.ant("/*"))
				.build();*/
		
		/*return new Docket(DocumentationType.SWAGGER_2)
		            .groupName("customer-api")
		            .useDefaultResponseMessages(false)
		            .apiInfo(apiInfo())
		            .select()
		                //.paths(PathSelectors.ant("/customers/*"))
		                .paths(PathSelectors.ant("/customers*"))
		            .build();*/
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("Customer API", "API for customer management", "1.0", "Terms of service",
				new Contact("Ravi Panchal", "www.ca.com", "ravi@ca.com"), "License of API", "API license URL",
				Collections.emptyList());
	}

}
