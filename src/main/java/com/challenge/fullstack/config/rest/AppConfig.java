package com.challenge.fullstack.config.rest;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        // Configurar cliente HttpClient con Apache HttpClient 5.x
        CloseableHttpClient httpClient = HttpClients.custom()
                .evictExpiredConnections() // Eliminar conexiones caducadas
                .evictIdleConnections(TimeValue.ofSeconds(30)) // Eliminar conexiones inactivas después de 30 segundos
                .build();

        // Configurar HttpComponentsClientHttpRequestFactory con tiempos de espera
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setConnectTimeout(10000); // Tiempo de conexión: 10 segundos
        requestFactory.setReadTimeout(30000);    // Tiempo de lectura: 30 segundos

        return new RestTemplate(requestFactory);
    }
}
