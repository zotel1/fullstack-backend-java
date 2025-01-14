package com.challenge.fullstack.service;

import com.challenge.fullstack.dto.CountryDto;
import com.challenge.fullstack.dto.RestCountryDto;
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
    private final CountryApiService countryApiService;

    public CountryService(CountryRepository countryRepository, CountryApiService countryApiService) {
        this.countryRepository = countryRepository;
        this.countryApiService = countryApiService;
    }

    public void fetchSaveCountries() {
        List<RestCountryDto> countries = countryApiService.fetchCountriesFromApi();

        // Mapear los datos a tu entidad `Country`
        List<Country> entities = countries.stream()
                .map(dto -> new Country(null, dto.getName().getCommon(), dto.getFlags().getPng()))
                .collect(Collectors.toList());

        // Guardar los países en la base de datos
        countryRepository.saveAll(entities);
    }
}
