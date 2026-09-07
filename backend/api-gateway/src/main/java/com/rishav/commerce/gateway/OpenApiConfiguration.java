package com.rishav.commerce.gateway;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
  @Bean
  OpenAPI gatewayOpenApi() {
    return new OpenAPI()
        .info(
            new Info()
                .title("EventFlow Commerce API Gateway")
                .version("v1")
                .description(
                    "Central API documentation for the EventFlow Commerce microservices."));
  }
}
