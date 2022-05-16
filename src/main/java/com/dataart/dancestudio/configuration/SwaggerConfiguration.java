package com.dataart.dancestudio.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfiguration {

    /**
     * Swagger URL:
     * 1. http://localhost:8080/swagger-ui/
     * 2. http://localhost:8080/swagger-ui/index.html
     * 3. http://localhost:8080/v2/api-docs
     **/

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.dataart.dancestudio.rest"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Dance Studio API")
                .description("DataArt Internship Project.")
                .contact(new Contact("Ekaterina Selivanova", "https://github.com/katseliv", "Ekaterina.Selivanova@dataart.com"))
                .version("1.0")
                .build();
    }

}
