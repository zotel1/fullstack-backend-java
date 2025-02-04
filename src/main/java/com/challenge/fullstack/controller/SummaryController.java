package com.challenge.fullstack.controller;

import com.challenge.fullstack.service.PlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1")
public class SummaryController {

    @Autowired
    private PlantService plantService;

    @GetMapping("/summary")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("cantidadLecturas", plantService.getCantidadLecturasCount());
        summary.put("alertasMedias", plantService.getAlertasMediasCount());
        summary.put("alertasRojas", plantService.getAlertasRojasCount());
        summary.put("sensoresInactivos", plantService.getSensoresInactivosCount());

        return ResponseEntity.ok(summary);
    }
}