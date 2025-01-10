package com.challenge.fullstack.controller;

import com.challenge.fullstack.model.PlantModel;
import com.challenge.fullstack.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@CrossOrigin("*")
@RestController
@RequestMapping("api/v1/plants")
public class PlantController {

    @Autowired
    PlantService plantService;

    // Controlador para obtener todas las plantas
    @GetMapping
    public ResponseEntity<List<PlantModel>> getAllPlants() {
        return ResponseEntity.ok(plantService.findAll());
    }

    // Controlador para crear una nueva planta
    @PostMapping
    public ResponseEntity<PlantModel> createPlant(@RequestBody Map<String, Object> payload) {
        String nombre = (String) payload.get("nombre");
        Long paisId = Long.valueOf(payload.get("paisId").toString());
        PlantModel plant = plantService.createPlant(nombre, paisId);
        return ResponseEntity.status(HttpStatus.CREATED).body(plant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlantModel> updatePlant(
            @PathVariable Long id,
            @RequestBody Map<String, Object> payload) {
        String nombre = (String) payload.get("nombre");
        Long paisId = payload.get("paisId") != null ? Long.valueOf(payload.get("paisId").toString()) : null;
        Integer cantidadLecturas = payload.get("cantidadLecturas") != null ? Integer.valueOf(payload.get("cantidadLecturas").toString()) : null;
        Integer alertasMedias = payload.get("alertasMedias") != null ? Integer.valueOf(payload.get("alertasMedias").toString()) : null;
        Integer alertasRojas = payload.get("alertasRojas") != null ? Integer.valueOf(payload.get("alertasRojas").toString()) : null;

        PlantModel updatePlant = plantService.updatePlant(id, nombre, paisId, cantidadLecturas, alertasMedias, alertasRojas);
        return ResponseEntity.ok(updatePlant);
    }

    @DeleteMapping("/{id")
    public ResponseEntity<Void> deletePlant(@PathVariable Long id) {
        plantService.deletePlant(id);
        return ResponseEntity.noContent().build();
    }

}
