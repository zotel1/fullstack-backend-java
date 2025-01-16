package com.challenge.fullstack.service;
import com.challenge.fullstack.dto.MinimalCountryDto;
import com.challenge.fullstack.dto.RestCountryDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

@Service
public class CountryApiService {

    private final RestTemplate restTemplate;
    private static final String API_URL = "https://restcountries.com/v3.1/all?fields=name,flags";

    public CountryApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<MinimalCountryDto> fetchCountriesFromApi() {
        try {
            String response = restTemplate.getForObject(API_URL, String.class);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<MinimalCountryDto>>() {}.getType();
            return gson.fromJson(response, listType);
        } catch (Exception e) {
            throw new RuntimeException("Error al consumir la API externa: " + e.getMessage(), e);
        }
    }
}
