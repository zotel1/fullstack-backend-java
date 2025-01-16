package com.challenge.fullstack.config.rest;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        // Configuración del pool de conexiones
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100); // Máximo de conexiones totales
        connectionManager.setDefaultMaxPerRoute(50); // Máximo de conexiones por ruta

        // Configuración de tiempos de espera
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(30)) // Tiempo para establecer conexión
                .setResponseTimeout(Timeout.ofSeconds(120)) // Tiempo de espera para respuesta
                .setConnectionRequestTimeout(Timeout.ofSeconds(30)) // Tiempo para obtener conexión del pool
                .build();

        // Configuración del cliente HTTP
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections() // Eliminar conexiones caducadas
                .evictIdleConnections(TimeValue.ofSeconds(60)) // Eliminar conexiones inactivas después de 60 segundos
                .build();

        // Crear RestTemplate con HttpClient
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(requestFactory);
    }
}
