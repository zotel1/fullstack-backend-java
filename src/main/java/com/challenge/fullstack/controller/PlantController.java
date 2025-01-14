package com.challenge.fullstack.controller;

import com.challenge.fullstack.dto.PlantDto;
import com.challenge.fullstack.model.PlantModel;
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

    @GetMapping("/list") // Subruta específica para obtener todas las plantas
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<PlantDto>> getAllPlants() {
        return ResponseEntity.ok(plantService.findAll());
    }

    // Controlador para crear una nueva planta
    @PostMapping("/create")
    @Operation(summary = "Cree una nueva planta", description = "Agregue una nueva planta al sistema con un país seleccionado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Planta creada"),
            @ApiResponse(responseCode = "400", description = "Entrada inválida"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> createPlant(@RequestBody Map<String, Object> payload) {
        try {
            String nombre = (String) payload.get("nombre");
            String countryName = (String) payload.get("countryName");

            // Crear planta y manejar la lógica de país
            PlantModel plant = plantService.createPlant(nombre, countryName);
            return ResponseEntity.status(HttpStatus.CREATED).body(plant);

        } catch (Exception e) {
            // Manejo de excepciones
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la planta: " + e.getMessage());
        }
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
