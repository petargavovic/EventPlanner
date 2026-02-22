package com.events.planner.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI plannerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Planner API")
                        .version("1.0.0")
                        .description("Event Planner REST API"));
    }
}