package com.challenge.fullstack.controller;

import com.challenge.fullstack.dto.CountryDto;
import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.repository.CountryRepository;
import com.challenge.fullstack.service.CountryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@SecurityRequirement(name = "bearer-key")
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class CountryController {

    private final CountryService countryService;
    private final CountryRepository countryRepository;

    public CountryController(CountryService countryService, CountryRepository countryRepository) {
        this.countryService = countryService;
        this.countryRepository = countryRepository;
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/countries")
    public ResponseEntity<List<CountryDto>> getCountries() {
        List<Country> countries = countryRepository.findAll();
        System.out.println("Países obtenidos de la base de datos: " + countries.size());

        List<CountryDto> response = countries.stream()
                .map(country -> new CountryDto(
                        new CountryDto.Name(country.getName()),
                        new CountryDto.Flags(country.getFlagUrl())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    @PutMapping("/countries/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> fetchCountries() {
        try {
            countryService.fetchSaveCountries();
            return ResponseEntity.ok("Países actualizados correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar los países: " + e.getMessage());
        }
    }
}
