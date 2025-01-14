package com.challenge.fullstack.service;

import com.challenge.fullstack.dto.CountryDto;
import com.challenge.fullstack.dto.RestCountryDto;
import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.repository.CountryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
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

    @Autowired
    public CountryService(CountryRepository countryRepository, RestTemplate restTemplate) {
        this.countryRepository = countryRepository;
        this.restTemplate = restTemplate;
    }

    // Método para consumir la API y guardar países en la base de datos
    @PostConstruct
    public void initializeCountries() {
        if (countryRepository.count() > 0) {
            System.out.println("Los países ya están almacenados en la base de datos.");
            return;
        }

        String url = "https://restcountries.com/v3.1/all";
        try {
            ResponseEntity<CountryDto[]> response = restTemplate.getForEntity(url, CountryDto[].class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<CountryDto> countryDtos = Arrays.asList(response.getBody());

                List<Country> countries = countryDtos.stream()
                        .map(dto -> new Country(null, dto.getName().getCommon(), dto.getFlags().getPng()))
                        .collect(Collectors.toList());

                countryRepository.saveAll(countries);
                System.out.println("Países almacenados correctamente.");
            } else {
                System.err.println("Error al consumir la API de países. Código de estado: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error al consumir la API de países: " + e.getMessage());
        }
    }
}
