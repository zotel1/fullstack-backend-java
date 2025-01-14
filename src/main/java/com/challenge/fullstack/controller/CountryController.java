package com.challenge.fullstack.controller;

import com.challenge.fullstack.dto.CountryDto;
import com.challenge.fullstack.repository.CountryRepository;
import com.challenge.fullstack.service.CountryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<List<CountryDto>> getCountries() {
        try {
            List<CountryDto> countries = countryService.getCountriesFromApi();
            return ResponseEntity.ok(countries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}


   /* @PutMapping("/countries/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> fetchCountries() {
        try {
            countryService.findOrCreateCountry(c);
            return ResponseEntity.ok("Países actualizados correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar los países: " + e.getMessage());
        }
    }*/


