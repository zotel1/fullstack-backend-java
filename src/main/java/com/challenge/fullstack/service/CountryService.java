package com.challenge.fullstack.service;

import com.challenge.fullstack.dto.CountryDto;
import com.challenge.fullstack.dto.RestCountryDto;
import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.repository.CountryRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private final RestTemplate restTemplate;

    public CountryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    private CountryRepository countryRepository;

    private static final String REST_COUNTRIES_API = "https://restcountries.com/v3.1/all";

    // Método para obtener la lista de países desde la API externa
    public List<CountryDto> getCountriesFromApi() {
        try {
            System.out.println("Consumiendo API externa para obtener países...");

            String response = restTemplate.getForObject(REST_COUNTRIES_API, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, new TypeReference<List<CountryDto>>() {});

        } catch (Exception e) {
            throw new RuntimeException("Error al consumir la API externa de países: " + e.getMessage());
        }
    }

    // Método para guardar país en la base de datos al crear una planta
    public Country findOrCreateCountry(String countryName) {
        return countryRepository.findByName(countryName).orElseGet(() -> {
            try {
                List<CountryDto> countries = getCountriesFromApi();
                CountryDto matchingCountry = countries.stream()
                        .filter(dto -> dto.getName().getCommon().equalsIgnoreCase(countryName))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("País no encontrado en la API externa"));

                Country newCountry = new Country();
                newCountry.setName(matchingCountry.getName().getCommon());
                newCountry.setFlagUrl(matchingCountry.getFlags().getPng());
                return countryRepository.save(newCountry);

            } catch (Exception e) {
                throw new RuntimeException("Error al guardar el país: " + e.getMessage());
            }
        });
    }
}
