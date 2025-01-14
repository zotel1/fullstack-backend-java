package com.challenge.fullstack.service;

import com.challenge.fullstack.dto.CountryDto;
import com.challenge.fullstack.dto.PlantDto;
import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.model.PlantModel;
import com.challenge.fullstack.repository.CountryRepository;
import com.challenge.fullstack.repository.IPlantRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@Service
public class PlantService {

    private final IPlantRepository iPlantRepository;
    private final CountryRepository countryRepository;

    public PlantService(IPlantRepository iPlantRepository, CountryRepository countryRepository) {
        this.iPlantRepository = iPlantRepository;
        this.countryRepository = countryRepository;
    }

    public List<PlantDto> findAll() {
        return iPlantRepository.findAll().stream()
                .map(plant -> new PlantDto(
                        plant.getId(),
                        plant.getNombre(),
                        plant.getCountry().getName(),
                        plant.getCountry().getFlagUrl(),
                        plant.getCantidadLecturas(),
                        plant.getAlertasMedias(),
                        plant.getAlertasRojas()
                ))
                .collect(Collectors.toList());
    }

    public PlantModel createPlant(String nombre, String countryName) {
        Country country = countryRepository.findByName(countryName)
                .orElseThrow(() -> new RuntimeException("País no encontrado: " + countryName));

        PlantModel plant = new PlantModel();
        plant.setNombre(nombre);
        plant.setCountry(country);
        plant.setCantidadLecturas((int) (Math.random() * 100));
        plant.setAlertasMedias((int) (Math.random() * 50));
        plant.setAlertasRojas((int) (Math.random() * 20));

        return iPlantRepository.save(plant);
    }
}
