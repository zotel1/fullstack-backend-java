package com.challenge.fullstack.service;

import com.challenge.fullstack.dto.CountryDto;
import com.challenge.fullstack.dto.MinimalCountryDto;
import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.repository.CountryRepository;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.context.event.ApplicationReadyEvent;


import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final RestTemplate restTemplate;
    private final Gson gson;

    @Value("${external.api.url:https://restcountries.com/v3.1/all}")
    private String REST_COUNTRIES_API_URL;

    public CountryService(CountryRepository countryRepository, RestTemplate restTemplate, Gson gson) {
        this.countryRepository = countryRepository;
        this.restTemplate = restTemplate;
        this.gson = gson;
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

            if (response == null || response.isEmpty()) {
                throw new RuntimeException("La respuesta de la API es vacía o nula");
            }

            // Parsear JSON con Gson
            Type listType = new TypeToken<List<MinimalCountryDto>>() {}.getType();
            List<MinimalCountryDto> countryDtos = gson.fromJson(response, listType);

            if (countryDtos == null || countryDtos.isEmpty()) {
                throw new RuntimeException("No se pudieron parsear los datos de la API");
            }

            // Filtrar y mapear datos relevantes
            List<Country> countries = countryDtos.stream()
                    .filter(dto -> dto.getName() != null && dto.getName().getCommon() != null && dto.getFlags() != null)
                    .map(dto -> new Country(null, dto.getName().getCommon(), dto.getFlags().getPng()))
                    .collect(Collectors.toList());

            if (countries.isEmpty()) {
                System.out.println("No se encontraron países válidos en la respuesta.");
            } else {
                countryRepository.saveAll(countries);
                System.out.println("Países almacenados correctamente: " + countries.size());
            }
        } catch (JsonSyntaxException e) {
            System.err.println("Error al parsear JSON: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error general: " + e.getMessage());
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

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        System.out.println("Aplicación lista. Iniciando carga de países...");
        initializeCountries();
    }
}
