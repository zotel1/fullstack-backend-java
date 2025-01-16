package com.challenge.fullstack.service;

import com.challenge.fullstack.dto.CountryDto;
import com.challenge.fullstack.dto.PlantDto;
import com.challenge.fullstack.dto.RestCountryDto;
import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.model.PlantModel;
import com.challenge.fullstack.repository.CountryRepository;
import com.challenge.fullstack.repository.IPlantRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final CountryApiService countryApiService;

    public CountryService(CountryRepository countryRepository, CountryApiService countryApiService) {
        this.countryRepository = countryRepository;
        this.countryApiService = countryApiService;
    }

    @PostConstruct
    public void initializeCountries() {
        if (countryRepository.count() > 0) {
            System.out.println("Los países ya están en la base de datos.");
            return;
        }

        System.out.println("Iniciando la carga de países desde la API externa...");
        List<RestCountryDto> countryDtos = countryApiService.fetchCountriesFromApi();

        List<Country> countries = countryDtos.stream()
                .filter(dto -> dto.getName() != null && dto.getName().getCommon() != null && dto.getFlags() != null)
                .map(dto -> new Country(null, dto.getName().getCommon(), dto.getFlags().getPng()))
                .toList();

        countryRepository.saveAll(countries);
        System.out.println("Países almacenados correctamente.");
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
