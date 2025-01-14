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
            ResponseEntity<CountryDto[]> response = restTemplate.getForEntity(url, CountryDto[].class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<CountryDto> countries = Arrays.asList(response.getBody());
                System.out.println("Países obtenidos de la API: " + countries.size());

                for (CountryDto dto : countries) {
                    String countryName = dto.getName().getCommon();
                    String flagUrl = dto.getFlags().getPng();

                    System.out.println("Procesando país: " + countryName + ", URL de bandera: " + flagUrl);

                    // Verifica si el país ya existe
                    if (!countryRepository.existsByName(countryName)) {
                        Country country = new Country(null, countryName, flagUrl);
                        countryRepository.save(country);
                        System.out.println("País guardado: " + countryName);
                    } else {
                        System.out.println("El país ya existe: " + countryName);
                    }
                }
            } else {
                throw new RuntimeException("Error al obtener países: Código " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error al consumir la API externa: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }



}
