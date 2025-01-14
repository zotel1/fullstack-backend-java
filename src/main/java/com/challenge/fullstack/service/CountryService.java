package com.challenge.fullstack.service;

import com.challenge.fullstack.dto.CountryDto;
import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.repository.CountryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public void fetchSaveCountries() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://restcountries.com/v3.1/all";

        try {
            System.out.println("Consumiendo la API externa...");
            ResponseEntity<CountryDto[]> response = restTemplate.getForEntity(url, CountryDto[].class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<CountryDto> countries = Arrays.asList(response.getBody());
                System.out.println("Cantidad de países obtenidos: " + countries.size());

                List<Country> entities = countries.stream()
                        .map(dto -> {
                            System.out.println("Procesando país: " + dto.getName().getCommon());
                            return new Country(null, dto.getName().getCommon(), dto.getFlags().getPng());
                        })
                        .collect(Collectors.toList());

                System.out.println("Guardando países en la base de datos...");
                countryRepository.saveAll(entities);
                System.out.println("Países guardados exitosamente.");
            } else {
                System.err.println("Error al obtener países: Código " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error al consumir la API externa: " + e.getMessage());
            throw new RuntimeException("Error al consumir la API externa: " + e.getMessage());
        }
    }

}
