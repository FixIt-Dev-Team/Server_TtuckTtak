package com.service.ttucktak.config;

import com.service.ttucktak.config.security.CustomHttpHeaders;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger(Spring Docs) 설정
 * */
@OpenAPIDefinition(
        info = @Info(title = "뚝딱 서비스 API 명세서",
        description = "뚝딱 서비스 API 명세서",
        version = "1.0.0")
)
@RequiredArgsConstructor
@Configuration
public class SpringDocsConfig {

    public OpenApiCustomizer securityBudiler() {
        return OpenAPI -> OpenAPI.addSecurityItem(new SecurityRequirement().addList("Access Token"))
                .getComponents()
                .addSecuritySchemes("Access Token", new SecurityScheme()
                        .name(CustomHttpHeaders.AUTHORIZATION)
                        .type(SecurityScheme.Type.HTTP)
                        .in(SecurityScheme.In.HEADER)
                        .bearerFormat("JWT")
                        .scheme("bearer"));
    }
    @Bean
    public GroupedOpenApi openApi(){
        String[] paths = {"/api/**"};

        return GroupedOpenApi.builder()
                .group("뚝딱 서비스 API")
                .pathsToMatch(paths)
                .addOpenApiCustomizer(securityBudiler())
                .build();
    }
}
