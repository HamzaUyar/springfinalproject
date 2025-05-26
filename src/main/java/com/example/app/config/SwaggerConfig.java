package com.example.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springdoc.core.GroupedOpenApi; // Not strictly needed for basic setup with springdoc-openapi-starter-webmvc-ui

@Configuration
// @EnableOpenApi // This annotation is deprecated and not needed with springdoc-openapi v2+
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Product & Category API")
                        .version("1.0.0")
                        .description("CRUD operations for products and categories."));
    }

    /* Optional: If you need to group APIs, you can use GroupedOpenApi
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("springshop-public")
                .pathsToMatch("/api/**")
                .build();
    }
    */
} 