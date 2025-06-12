package com.example.demodockerfile.config_app;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "Mi API Personalizada",
        version = "1.0",
        description = "Documentación de mi API con SpringDoc"
    )
)
@Configuration
public class OpenApiConfig {
}
