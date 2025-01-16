package com.challenge.fullstack.config.rest;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import org.apache.hc.core5.util.Timeout;
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        // Configuración del pool de conexiones
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(50); // Máximo de conexiones totales
        connectionManager.setDefaultMaxPerRoute(20); // Máximo de conexiones por ruta

        // Configuración del cliente HTTP con tiempos de espera
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(15)) // Tiempo de conexión: 15 segundos
                .setResponseTimeout(Timeout.ofSeconds(60)) // Tiempo de espera de socket: 60 segundos
                .setConnectionRequestTimeout(Timeout.ofSeconds(15)) // Tiempo de espera de solicitud: 15 segundos
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections() // Eliminar conexiones caducadas
                .evictIdleConnections(TimeValue.ofSeconds(30)) // Eliminar conexiones inactivas tras 30 segundos
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        // Lógica de reintento
        restTemplate.getInterceptors().add((request, body, execution) -> {
            for (int i = 0; i < 3; i++) {
                try {
                    return execution.execute(request, body);
                } catch (IOException ex) {
                    if (i == 2) throw ex; // Lanza la excepción si falla tras 3 intentos
                }
            }
            return null;
        });

        return restTemplate;
    }
}
