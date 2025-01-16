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
            ResponseEntity<String> response = restTemplate.getForEntity(REST_COUNTRIES_API_URL, String.class);

            // Registra la respuesta JSON
            System.out.println("Respuesta de la API de países:");
            System.out.println(response.getBody());

            // Deserializa la respuesta manualmente
            ObjectMapper objectMapper = new ObjectMapper();
            RestCountryDto[] countryDtos = objectMapper.readValue(response.getBody(), RestCountryDto[].class);

            // Guarda los países
            List<Country> countries = Arrays.stream(countryDtos)
                    .filter(dto -> dto.getName() != null && dto.getName().getCommon() != null && dto.getFlags() != null)
                    .map(dto -> new Country(null, dto.getName().getCommon(), dto.getFlags().getPng()))
                    .collect(Collectors.toList());


            countryRepository.saveAll(countries);
            System.out.println("Países almacenados correctamente.");
        } catch (Exception e) {
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
                .filter(country -> country.getName() != null && country.getFlagUrl() != null)
                .map(country -> new CountryDto(
                        new CountryDto.Name(country.getName()),
                        new CountryDto.Flags(country.getFlagUrl())
                ))
                .collect(Collectors.toList());
    }

}
