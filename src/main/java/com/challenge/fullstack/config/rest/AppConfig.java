package com.challenge.fullstack.config.rest;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.apache.hc.core5.http.message.BasicHeader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        // Configuración del pool de conexiones
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);
        connectionManager.setDefaultMaxPerRoute(50);

        // Configuración de tiempos de espera
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofMinutes(2)) // Tiempo de conexión
                .setResponseTimeout(Timeout.ofMinutes(4)) // Tiempo de espera para respuesta
                .setConnectionRequestTimeout(Timeout.ofMinutes(1)) // Tiempo para obtener conexión
                .build();

        // Configuración del cliente HTTP
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .evictExpiredConnections()
                .evictIdleConnections(TimeValue.ofMinutes(1)) // Tiempo para eliminar conexiones inactivas
                .setDefaultHeaders(Collections.singletonList(new BasicHeader("Accept-Encoding", "gzip")))
                .build();

        // Crear RestTemplate con HttpClient
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(requestFactory);
    }
}
