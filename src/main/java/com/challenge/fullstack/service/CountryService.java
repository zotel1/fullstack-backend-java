package com.challenge.fullstack.service;

import com.challenge.fullstack.dto.CountryDto;
import com.challenge.fullstack.dto.PlantDto;
import com.challenge.fullstack.dto.RestCountryDto;
import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.model.PlantModel;
import com.challenge.fullstack.repository.CountryRepository;
import com.challenge.fullstack.repository.IPlantRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final RestTemplate restTemplate;
    private static final String REST_COUNTRIES_API_URL = "https://restcountries.com/v3.1/all";

    public CountryService(CountryRepository countryRepository, RestTemplate restTemplate) {
        this.countryRepository = countryRepository;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void initializeCountries() {
        if (countryRepository.count() > 0) {
            System.out.println("Los países ya están en la base de datos.");
            return;
        }

        System.out.println("Iniciando la carga de países desde la API externa...");
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(REST_COUNTRIES_API_URL, String.class);

                if (!response.getStatusCode().is2xxSuccessful()) {
                    throw new RuntimeException("Respuesta no exitosa: " + response.getStatusCode());
                }

                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                RestCountryDto[] countryDtos = objectMapper.readValue(response.getBody(), RestCountryDto[].class);

                List<Country> countries = Arrays.stream(countryDtos)
                        .filter(dto -> dto.getName() != null && dto.getName().getCommon() != null && dto.getFlags() != null)
                        .map(dto -> new Country(null, dto.getName().getCommon(), dto.getFlags().getPng()))
                        .collect(Collectors.toList());

                countryRepository.saveAll(countries);
                System.out.println("Países almacenados correctamente.");
                return; // Salir del bucle si la solicitud fue exitosa
            } catch (Exception e) {
                System.err.println("Intento " + attempt + " fallido: " + e.getMessage());
                if (attempt == 3) {
                    System.err.println("Error al consumir la API externa después de 3 intentos");
                    e.printStackTrace();
                }
            }
        }
    }
}
