package com.challenge.fullstack.controller;

import com.challenge.fullstack.dto.PlantDto;
import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.model.PlantModel;
import com.challenge.fullstack.repository.CountryRepository;
import com.challenge.fullstack.repository.IPlantRepository;
import com.challenge.fullstack.service.JwtTokenService;
import com.challenge.fullstack.service.PlantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "bearer-key")
@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/plants")
public class PlantController {

    private final PlantService plantService;
    private final JwtTokenService jwtTokenService;

    public PlantController(PlantService plantService, JwtTokenService jwtTokenService) {
        this.plantService = plantService;
        this.jwtTokenService = jwtTokenService;
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<PlantDto>> getUserPlants(@RequestHeader("Authorization") String token) {
        String username = jwtTokenService.extractUsername(token.substring(7));
        return ResponseEntity.ok(plantService.findAll(username));
    }

    @GetMapping("/list-paginated")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Page<PlantDto>> getPaginatedPlants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestHeader("Authorization") String token) {
        String username = jwtTokenService.extractUsername(token.substring(7));
        Page<PlantDto> plants = plantService.findAllPaginated(username, page, size);
        return ResponseEntity.ok(plants);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> createPlant(@RequestBody Map<String, Object> payload, @RequestHeader("Authorization") String token) {
        try {
            String username = jwtTokenService.extractUsername(token.substring(7));
            String nombre = (String) payload.get("nombre");
            String countryName = (String) payload.get("countryName");

            if (nombre == null || countryName == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Faltan campos obligatorios.");
            }

            PlantModel plant = plantService.createPlant(nombre, countryName, username);
            return ResponseEntity.status(HttpStatus.CREATED).body(plant);
        }catch (RuntimeException e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la planta: " + e.getMessage());
        }
    }
}
