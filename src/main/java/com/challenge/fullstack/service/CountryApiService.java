package com.challenge.fullstack.service;
import com.challenge.fullstack.dto.RestCountryDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class CountryApiService {

    private final RestTemplate restTemplate;
    private static final String API_URL = "https://restcountries.com/v3.1/all";

    public CountryApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<RestCountryDto> fetchCountriesFromApi() {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(API_URL, String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("API respondió con error: " + response.getStatusCode());
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            RestCountryDto[] countries = objectMapper.readValue(response.getBody(), RestCountryDto[].class);
            return Arrays.asList(countries);
        } catch (Exception e) {
            throw new RuntimeException("Error al consumir la API externa: " + e.getMessage(), e);
        }
    }
}
