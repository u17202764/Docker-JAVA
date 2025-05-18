package com.example.demodockerfile.config_app;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${ruta.imagenes}") // Define esta propiedad en application.properties o application.yml
    private String rutaImagenes;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        try {

            registry.addResourceHandler("/images/**")
                    .addResourceLocations("file:" + rutaImagenes);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }





}
