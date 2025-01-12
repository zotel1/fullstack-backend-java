package com.challenge.fullstack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class MyConfigurationCors {

    @Bean(name = "customCorsConfigurationSource")
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true); // Permite enviar cookies y credenciales
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200", // Desarrollo local
                "https://frontend-angular-du7wr8sfl-cristians-projects-3ed964a9.vercel.app",
                "https://frontend-angular-zeta.vercel.app/login",
                "https://forbtech-front.vercel.app/login",
                "https://forbtech-front.vercel.app",// Frontend en Vercel
                "https://frontend-angular-zeta.vercel.app" // Ajusta al dominio actual en Vercel
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Incluye OPTIONS para preflight
        configuration.setAllowedHeaders(Arrays.asList(
                "Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"
        )); // Asegúrate de listar todos los encabezados necesarios
        configuration.setExposedHeaders(Arrays.asList("Authorization")); // Permite acceder al header Authorization en la respuesta
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplica la configuración a todas las rutas
        return source;
    }
}
