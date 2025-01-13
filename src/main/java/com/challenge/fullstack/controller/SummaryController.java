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
@RequestMapping("/api/v1/summary")
public class SummaryController {

    @Autowired
    private PlantService plantService;

    @GetMapping("/list")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("readingsOk", plantService.getReadingsOkCount());
        summary.put("mediumAlerts", plantService.getMediumAlertsCount());
        summary.put("redAlerts", plantService.getRedAlertsCount());
        summary.put("disabledSensors", plantService.getDisabledSensorsCount());

        return ResponseEntity.ok(summary);
    }
}