package com.challenge.fullstack.controller;

import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.repository.CountryRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirement(name = "bearer-key")
@RequestMapping("/api/v1/countries")
@RestController
@Controller
public class CountryController {

    private final CountryRepository countryRepository;

    public CountryController(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @GetMapping
    public ResponseEntity<List<Country>> getAllCountries() {
        List<Country> countries = countryRepository.findAll();
        return ResponseEntity.ok(countries);
    }
}
