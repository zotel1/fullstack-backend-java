package com.challenge.fullstack.service;

import com.challenge.fullstack.dto.CountryDto;
import com.challenge.fullstack.dto.PlantDto;
import com.challenge.fullstack.dto.RestCountryDto;
import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.model.PlantModel;
import com.challenge.fullstack.repository.CountryRepository;
import com.challenge.fullstack.repository.IPlantRepository;
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

    // URL de la API externa
    private static final String REST_COUNTRIES_API_URL = "https://restcountries.com/v3.1/all";

    public CountryService(CountryRepository countryRepository, RestTemplate restTemplate) {
        this.countryRepository = countryRepository;
        this.restTemplate = restTemplate;
    }

    /**
     * Inicializa los países en la base de datos desde la API externa.
     * Se ejecuta automáticamente cuando se levanta el contexto de la aplicación.
     */
    @PostConstruct
    public void initializeCountries() {
        if (countryRepository.count() > 0) {
            System.out.println("Los países ya están en la base de datos.");
            return;
        }

        System.out.println("Iniciando la carga de países desde la API externa...");
        try {
            // Realiza la solicitud a la API externa
            ResponseEntity<CountryDto[]> response = restTemplate.getForEntity(REST_COUNTRIES_API_URL, CountryDto[].class);

            // Verifica si la respuesta es exitosa
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<CountryDto> countryDtos = Arrays.asList(response.getBody());

                // Mapea los datos de la API a entidades de la base de datos
                List<Country> countries = countryDtos.stream()
                        .map(dto -> new Country(null, dto.getName().getCommon(), dto.getFlags().getPng()))
                        .collect(Collectors.toList());

                // Guarda los países en la base de datos
                countryRepository.saveAll(countries);
                System.out.println("Países almacenados correctamente.");
            } else {
                System.err.println("Error al consumir la API de países. Código de estado: " + response.getStatusCode());
            }
        } catch (Exception e) {
            // Manejo de errores en caso de fallos en la solicitud
            System.err.println("Error al consumir la API externa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Obtiene la lista de todos los países desde la base de datos.
     * @return Lista de países en formato DTO.
     */
    public List<CountryDto> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(country -> new CountryDto(
                        new CountryDto.Name(country.getName()),
                        new CountryDto.Flags(country.getFlagUrl())
                ))
                .collect(Collectors.toList());
    }
}

