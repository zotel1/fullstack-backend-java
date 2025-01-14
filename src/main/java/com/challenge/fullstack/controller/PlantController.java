package com.challenge.fullstack.controller;

import com.challenge.fullstack.dto.PlantDto;
import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.model.PlantModel;
import com.challenge.fullstack.repository.CountryRepository;
import com.challenge.fullstack.repository.IPlantRepository;
import com.challenge.fullstack.service.PlantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    PlantService plantService;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private IPlantRepository iPlantRepository;

    @GetMapping("/list") // Subruta específica para obtener todas las plantas
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<PlantDto>> getAllPlants() {
        return ResponseEntity.ok(plantService.findAll());
    }

    // Controlador para crear una nueva planta
    @PostMapping("/create")
    @Operation(summary = "Crear una nueva planta", description = "Crea una planta asociada a un país")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Planta creada"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public PlantModel createPlant(String nombre, String countryName) {
        Country country = countryRepository.findByName(countryName)
                .orElseThrow(() -> new RuntimeException("País no encontrado en la base de datos."));

        PlantModel plant = new PlantModel();
        plant.setNombre(nombre);
        plant.setCountry(country);
        plant.setCantidadLecturas((int) (Math.random() * 100)); // Generación aleatoria
        plant.setAlertasMedias((int) (Math.random() * 50));     // Generación aleatoria
        plant.setAlertasRojas((int) (Math.random() * 20));      // Generación aleatoria

        return iPlantRepository.save(plant);
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "Actualice una planta", description = "Actualice una planta con su respectivo país")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Planta actualizada"),
            @ApiResponse(responseCode = "404", description = "Planta no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updatePlant(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        try {
            String nombre = (String) payload.get("nombre");
            Long countryId = payload.get("countryId") != null ? Long.valueOf(payload.get("countryId").toString()) : null;
            Integer cantidadLecturas = payload.get("cantidadLecturas") != null ? Integer.valueOf(payload.get("cantidadLecturas").toString()) : null;
            Integer alertasMedias = payload.get("alertasMedias") != null ? Integer.valueOf(payload.get("alertasMedias").toString()) : null;
            Integer alertasRojas = payload.get("alertasRojas") != null ? Integer.valueOf(payload.get("alertasRojas").toString()) : null;

            PlantModel updatePlant = plantService.updatePlant(id, nombre, countryId, cantidadLecturas, alertasMedias, alertasRojas);
            return ResponseEntity.ok(updatePlant);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la planta: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deletePlant(@PathVariable Long id) {
        try {
            plantService.deletePlant(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la planta: " + e.getMessage());
        }
    }
}
