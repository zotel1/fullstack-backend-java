package com.challenge.fullstack.service;

import com.challenge.fullstack.dto.CountryDto;
import com.challenge.fullstack.dto.PlantDto;
import com.challenge.fullstack.dto.RestCountryDto;
import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.model.PlantModel;
import com.challenge.fullstack.repository.CountryRepository;
import com.challenge.fullstack.repository.IPlantRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import java.util.List;
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
        try {
            // Llamada a la API externa
            System.out.println("Llamando a la API externa: " + REST_COUNTRIES_API_URL);
            ResponseEntity<String> response = restTemplate.getForEntity(REST_COUNTRIES_API_URL, String.class);
            System.out.println("Estado de la respuesta: " + response.getStatusCode());

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Respuesta no exitosa: " + response.getStatusCode());
            }

            // Procesamiento de la respuesta JSON
            System.out.println("Procesando la respuesta de la API...");
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            RestCountryDto[] countryDtos = objectMapper.readValue(response.getBody(), RestCountryDto[].class);

            // Filtrado y transformación de datos
            List<Country> countries = Arrays.stream(countryDtos)
                    .filter(dto -> dto.getName() != null && dto.getName().getCommon() != null && dto.getFlags() != null)
                    .map(dto -> new Country(null, dto.getName().getCommon(), dto.getFlags().getPng()))
                    .toList();

            // Almacenamiento en la base de datos
            System.out.println("Guardando los países en la base de datos...");
            countryRepository.saveAll(countries);
            System.out.println("Países almacenados correctamente.");
        } catch (HttpClientErrorException e) {
            System.err.println("Error del cliente al consumir la API externa: " + e.getMessage());
        } catch (HttpServerErrorException e) {
            System.err.println("Error del servidor al consumir la API externa: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.err.println("Error al procesar la respuesta JSON: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error inesperado al inicializar países: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtiene todos los países desde la base de datos y los convierte en DTOs.
     *
     * @return Lista de CountryDto.
     */
    public List<CountryDto> getAllCountries() {
        System.out.println("Obteniendo todos los países de la base de datos...");
        return countryRepository.findAll().stream()
                .map(country -> new CountryDto(
                        new CountryDto.Name(country.getName()),
                        new CountryDto.Flags(country.getFlagUrl())
                ))
                .toList();
    }
}
