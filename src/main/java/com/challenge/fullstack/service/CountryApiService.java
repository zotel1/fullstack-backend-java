package com.challenge.fullstack.service;


import com.challenge.fullstack.dto.RestCountryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Service
public class CountryApiService {
    private static final String API_URL = "https://restcountries.com/v3.1/all";

    public List<RestCountryDto> fetchCountriesFromApi() {
        try {
            // Configuración de la conexión HTTP
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            // Leer la respuesta de la API
            ObjectMapper objectMapper = new ObjectMapper();
            RestCountryDto[] countries = objectMapper.readValue(connection.getInputStream(), RestCountryDto[].class);
            return Arrays.asList(countries);

        } catch (Exception e) {
            throw new RuntimeException("Error al consumir la API externa de países", e);
        }
    }
}