package com.service.ttucktak.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
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

    @Bean
    public GroupedOpenApi openApi(){
        String[] paths = {"/api/**"};

        return GroupedOpenApi.builder()
                .group("뚝딱 서비스 API")
                .pathsToMatch(paths)
                .build();
    }
}
