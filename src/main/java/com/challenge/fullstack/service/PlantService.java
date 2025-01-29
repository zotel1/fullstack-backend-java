package com.challenge.fullstack.service;

import com.challenge.fullstack.dto.CountryDto;
import com.challenge.fullstack.dto.PlantDto;
import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.model.PlantModel;
import com.challenge.fullstack.model.UserModel;
import com.challenge.fullstack.repository.CountryRepository;
import com.challenge.fullstack.repository.IPlantRepository;
import com.challenge.fullstack.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

@Service
public class PlantService {

    private final IPlantRepository iPlantRepository;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;

    public PlantService(IPlantRepository iPlantRepository, CountryRepository countryRepository, UserRepository userRepository) {
        this.iPlantRepository = iPlantRepository;
        this.countryRepository = countryRepository;
        this.userRepository = userRepository;
    }

    public List<PlantDto> findAll(String username) {
        UserModel user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }
        boolean isAdmin = "ADMIN".equals(user.getRole());

        return iPlantRepository.findPlantsForUser(user.getUser_id(), isAdmin).stream()
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

    public PlantModel createPlant(String nombre, String countryName, String username) {

        UserModel user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // Buscar el país en la base de datos
        Country country = countryRepository.findByName(countryName)
                .orElseThrow(() -> new RuntimeException("País no encontrado: " + countryName));

        // Crear una nueva planta
        PlantModel plant = new PlantModel();
        plant.setNombre(nombre);
        plant.setCountry(country);
        plant.setCantidadLecturas((int) (Math.random() * 100)); // Simulación de lecturas
        plant.setAlertasMedias((int) (Math.random() * 50)); // Simulación de alertas medias
        plant.setAlertasRojas((int) (Math.random() * 20)); // Simulación de alertas rojas
        plant.setCreatedBy(user);
        plant.setCreatedAt(new Date());

        return iPlantRepository.save(plant);
    }

    public int getReadingsOkCount() {
        return iPlantRepository.countByReadingsOk();
    }

    public int getMediumAlertsCount() {
        return iPlantRepository.countByMediumAlerts();
    }

    public int getRedAlertsCount() {
        return iPlantRepository.countByRedAlerts();
    }

    public int getDisabledSensorsCount() {
        return iPlantRepository.countByDisabledSensors();
    }
}
