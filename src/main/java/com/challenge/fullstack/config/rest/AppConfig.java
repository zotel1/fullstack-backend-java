package com.challenge.fullstack.config.rest;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
                .setConnectTimeout(Timeout.ofSeconds(30)) // Tiempo de conexión: 30 segundos
                .setResponseTimeout(Timeout.ofSeconds(60)) // Tiempo de espera de respuesta: 60 segundos
                .setConnectionRequestTimeout(Timeout.ofSeconds(30)) // Tiempo para solicitar una conexión: 30 segundos
                .build();

        // Configuración del cliente HTTP
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections() // Eliminar conexiones caducadas
                .evictIdleConnections(TimeValue.ofSeconds(60)) // Eliminar conexiones inactivas después de 60 segundos
                .build();

        // Crear fábrica de solicitudes HTTP
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        // Crear RestTemplate con la fábrica de solicitudes
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        // Añadir interceptor para lógica de reintento
        restTemplate.getInterceptors().add(retryInterceptor());

        return restTemplate;
    }

    // Interceptor para lógica de reintento
    private ClientHttpRequestInterceptor retryInterceptor() {
        return (request, body, execution) -> {
            for (int i = 0; i < 3; i++) { // Intentar 3 veces
                try {
                    return execution.execute(request, body);
                } catch (IOException ex) {
                    if (i == 2) throw ex; // Si falla después de 3 intentos, lanzar excepción
                }
            }
            return null;
        };
    }
}
