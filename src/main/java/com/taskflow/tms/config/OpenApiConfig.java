package com.taskflow.tms.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {


    @Bean
    public OpenAPI taskServiceOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("Task service api")
                        .description("APIS for managing tasks")
                        .version("v1.0.0")
                );
    }
}
