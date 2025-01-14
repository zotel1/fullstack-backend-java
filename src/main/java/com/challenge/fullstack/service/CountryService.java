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
    private final RestTemplate restTemplate;

    public CountryService(CountryRepository countryRepository, RestTemplate restTemplate) {
        this.countryRepository = countryRepository;
        this.restTemplate = restTemplate;
    }

    public void fetchSaveCountries() {
        String url = "https://restcountries.com/v3.1/all";

        try {
            ResponseEntity<CountryDto[]> response = restTemplate.getForEntity(url, CountryDto[].class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<CountryDto> countries = Arrays.asList(response.getBody());

                List<Country> entities = countries.stream()
                        .map(dto -> new Country(null, dto.getName().getCommon(), dto.getFlags().getPng()))
                        .collect(Collectors.toList());

                countryRepository.saveAll(entities);
            } else {
                throw new RuntimeException("Error al obtener países: Código " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al consumir la API externa: " + e.getMessage(), e);
        }
    }



}
