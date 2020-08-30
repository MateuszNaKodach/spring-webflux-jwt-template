package com.github.nowakprojects.springwebflux.jwttemplate.sharedkernel.infrastructure.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebFlux;

import java.util.List;

@Configuration
@EnableSwagger2WebFlux
public class SwaggerConfig {

    private static final String HEADER_PARAMETER_TYPE = "header";
    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Bean
    Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .build()
                .securitySchemes(List.of(apiKey()))
                .securityContexts(List.of(securityContext()));
    }


    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(path -> !path.contains("token"))
                .build();
    }

    public static List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope
                = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return List.of(new SecurityReference("JWT", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", AUTHORIZATION_HEADER, HEADER_PARAMETER_TYPE);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring WebFlux JWT Template - REST API")
                .version("1.0.0")
                .contact(new Contact("JWT Template", "github.com/nowakprojects", "kontakt.mateusznowak@gmail.com"))
                .build();
    }
}
