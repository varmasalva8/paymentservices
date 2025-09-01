package com.example.wallet.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
 @Bean
 public OpenAPI openAPI() {
     return new OpenAPI().info(new Info()
             .title("Payment Wallet API")
             .version("v1")
             .description("Week 3 REST endpoints with validation, HATEOAS and exception handling"));
 }
}



//---------------------
//NOTES
//- The controller builds simple HATEOAS self links. In production, compute links dynamically using WebMvcLinkBuilder.
//- Validation messages reflect the Week 3 spec (email format, positive amounts, etc.).
//- Swagger UI: http://localhost:8080/swagger-ui/index.html
//- Patch is supported by spring-web by default.
