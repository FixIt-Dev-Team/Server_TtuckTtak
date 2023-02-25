package com.service.ttucktak.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Configuration
@EnableAsync
@EnableWebMvc
@EnableSwagger2
public class SwaggerConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public Docket api() {
        Server localServer = new Server("local", "http://localhost:6543", "local test server", Collections.emptyList(), Collections.emptyList());
        Server serviceServer = new Server("service", "https://ttucktak.ljhhosting.com", "service server", Collections.emptyList(), Collections.emptyList());

        return new Docket(DocumentationType.OAS_30)
                .servers(localServer, serviceServer)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.service.ttucktak"))
                .paths(PathSelectors.any())
                .build()
                .directModelSubstitute(Date.class, java.sql.Date.class)
                .apiInfo(apiInfo())
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(securityScheme()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("TtuckTak")
                .description("TtuckTak")
                .version("1.0")
                .build();
    }

    private SecurityContext securityContext() {
        return springfox.documentation.spi.service.contexts
                .SecurityContext
                .builder()
                .securityReferences(defaultAuth())
                .operationSelector(operationContext -> true)
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("global", "accessEverything");
        return List.of(new SecurityReference("SERVER-ACCESS-TOKEN", authorizationScopes));
    }

    private ApiKey securityScheme() {
        String targetHeader = "SERVER-ACCESS-TOKEN";
        return new ApiKey("SERVER-ACCESS-TOKEN", targetHeader, "header");
    }
}

