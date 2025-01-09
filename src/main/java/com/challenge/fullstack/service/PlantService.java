package com.challenge.fullstack.service;

import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.model.PlantModel;
import com.challenge.fullstack.repository.CountryRepository;
import com.challenge.fullstack.repository.IPlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

//@Service
//public class PlantService implements IPlantService {
@Service
public class PlantService {

    @Autowired
    private IPlantRepository iPlantRepository;

    @Autowired
    private CountryRepository countryRepository;

    public List<PlantModel> findAll() {
        return iPlantRepository.findAll();
    }

    public PlantModel createPlant(String nombre, Long paisId) {
        Country pais = countryRepository.findById(paisId).orElseThrow(() -> new RuntimeException("Pais no encontrado."));
        PlantModel plant = new PlantModel();
        plant.setNombre(nombre);
        plant.setPais(pais);
        plant.setCantidadLecturas(0);
        plant.setAlertasMedias(0);
        plant.setAlertasRojas(0);
        return iPlantRepository.save(plant);
    }

}