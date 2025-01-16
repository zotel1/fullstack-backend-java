package com.challenge.fullstack.service;


import com.challenge.fullstack.dto.CountryDto;
import com.challenge.fullstack.dto.MinimalCountryDto;
import com.challenge.fullstack.dto.PlantDto;
import com.challenge.fullstack.dto.RestCountryDto;
import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.repository.CountryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final RestTemplate restTemplate;
    private static final String REST_COUNTRIES_API_URL = "https://restcountries.com/v3.1/all";
    private final Gson gson = new Gson();

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
        try {
            // Consumir la API externa
            String response = restTemplate.getForObject(REST_COUNTRIES_API_URL, String.class);

            // Parsear JSON con Gson
            Type listType = new TypeToken<List<MinimalCountryDto>>() {}.getType();
            List<MinimalCountryDto> countryDtos = gson.fromJson(response, listType);

            // Filtrar y mapear datos relevantes
            List<Country> countries = countryDtos.stream()
                    .filter(dto -> dto.getName() != null && dto.getName().getCommon() != null && dto.getFlags() != null)
                    .map(dto -> new Country(null, dto.getName().getCommon(), dto.getFlags().getPng()))
                    .collect(Collectors.toList());

            // Guardar en la base de datos
            countryRepository.saveAll(countries);
            System.out.println("Países almacenados correctamente.");
        } catch (Exception e) {
            System.err.println("Error al consumir la API externa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<CountryDto> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(country -> new CountryDto(
                        new CountryDto.Name(country.getName()),
                        new CountryDto.Flags(country.getFlagUrl())
                ))
                .toList();
    }
}
