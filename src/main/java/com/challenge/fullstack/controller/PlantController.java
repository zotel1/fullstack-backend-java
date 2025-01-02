package com.challenge.fullstack.controller;

import com.challenge.fullstack.model.PlantModel;
import com.challenge.fullstack.service.IPlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("api/v1")
public class PlantController {

    @Autowired
    IPlantService iPlantService;

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        List<PlantModel> plants = this.iPlantService.findAll();
        return new ResponseEntity<>(plants, HttpStatus.OK);
    }
}
