package com.challenge.fullstack.service;


import com.challenge.fullstack.dto.CountryDto;
import com.challenge.fullstack.dto.MinimalCountryDto;
import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.repository.CountryRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final RestTemplate restTemplate;
    private static final String REST_COUNTRIES_API_URL = "https://restcountries.com/v3.1/all";
    private static final int BATCH_SIZE = 10; // Número de países a procesar por lote

    public CountryService(CountryRepository countryRepository, RestTemplate restTemplate) {
        this.countryRepository = countryRepository;
        this.restTemplate = restTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeCountries() {
        if (countryRepository.count() > 0) {
            System.out.println("Los países ya están en la base de datos.");
            return;
        }

        System.out.println("Iniciando la carga de países desde la API externa...");

        try {
            // Realizar una solicitud inicial para obtener todos los datos
            String response = restTemplate.getForObject(REST_COUNTRIES_API_URL, String.class);

            // Parsear la respuesta JSON a una lista de objetos DTO
            Gson gson = new Gson();
            Type listType = new TypeToken<List<MinimalCountryDto>>() {}.getType();
            List<MinimalCountryDto> countryDtos = gson.fromJson(response, listType);

            // Procesar y guardar en lotes
            processInBatches(countryDtos);
        } catch (Exception e) {
            System.err.println("Error al consumir la API externa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processInBatches(List<MinimalCountryDto> countryDtos) {
        for (int i = 0; i < countryDtos.size(); i += BATCH_SIZE) {
            List<MinimalCountryDto> batch = countryDtos.subList(
                    i,
                    Math.min(i + BATCH_SIZE, countryDtos.size())
            );

            // Filtrar y mapear los datos del lote
            List<Country> countries = batch.stream()
                    .filter(dto -> dto.getName() != null && dto.getName().getCommon() != null && dto.getFlags() != null)
                    .map(dto -> new Country(null, dto.getName().getCommon(), dto.getFlags().getPng()))
                    .collect(Collectors.toList());

            // Guardar el lote en la base de datos
            countryRepository.saveAll(countries);
            System.out.println("Lote " + (i / BATCH_SIZE + 1) + " almacenado.");
        }

        System.out.println("Todos los países fueron almacenados correctamente.");
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
