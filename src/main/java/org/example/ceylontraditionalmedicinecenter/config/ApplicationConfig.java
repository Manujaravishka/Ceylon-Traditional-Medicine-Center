package org.example.ceylontraditionalmedicinecenter.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

// Declares this class as a source of Spring bean definitions.
@Configuration
//Spring Framework එකේ භාවිතා කරන annotation එකක්.
public class ApplicationConfig implements WebMvcConfigurer {

    // Registers the method return value as a Spring bean in the application context.
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setAmbiguityIgnored(true);
        return modelMapper;
    }

    // Registers the method return value as a Spring bean in the application context.
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // Indicates this method overrides a method from a superclass or interface.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/frontend/**")
                .addResourceLocations("classpath:/frontend/");
    }
}

