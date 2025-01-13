package com.challenge.fullstack.service;

import com.challenge.fullstack.dto.PlantDto;
import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.model.PlantModel;
import com.challenge.fullstack.repository.CountryRepository;
import com.challenge.fullstack.repository.IPlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class PlantService {

    @Autowired
    private IPlantRepository iPlantRepository;

    @Autowired
    private CountryRepository countryRepository;

    //public List<PlantModel> findAll() {
      //  return iPlantRepository.findAll();
    //}




    public int getReadingsOkCount() {
        return iPlantRepository.countByReadingsOk(); // Define este método en el repositorio
    }

    public int getMediumAlertsCount() {
        return iPlantRepository.countByMediumAlerts(); // Define este método en el repositorio
    }

    public int getRedAlertsCount() {
        return iPlantRepository.countByRedAlerts(); // Define este método en el repositorio
    }

    public int getDisabledSensorsCount() {
        return iPlantRepository.countByDisabledSensors(); // Define este método en el repositorio
    }

    public List<PlantDto> findAll() {
        return iPlantRepository.findAll().stream()
                .map(plant -> new PlantDto(
                        plant.getId(),
                        plant.getNombre(),
                        plant.getPais().getName(),
                        plant.getPais().getFlagUrl(),
                        plant.getCantidadLecturas(),
                        plant.getAlertasMedias(),
                        plant.getAlertasRojas()
                ))
                .collect(Collectors.toList());
    }

    public PlantModel createPlant(String nombre, Long paisId) {
        Country pais = countryRepository.findById(paisId)
                .orElseThrow(() -> new RuntimeException("Pais no encontrado."));
        PlantModel plant = new PlantModel();
        plant.setNombre(nombre);
        plant.setPais(pais);
        plant.setCantidadLecturas((int) (Math.random() * 100)); // Generación aleatoria
        plant.setAlertasMedias((int) (Math.random() * 50));     // Generación aleatoria
        plant.setAlertasRojas((int) (Math.random() * 20));      // Generación aleatoria
        return iPlantRepository.save(plant);
    }

    public PlantModel updatePlant(Long id, String nombre, Long paisId, Integer cantidadLecturas, Integer alertasMedias, Integer alertasRojas) {
        PlantModel plant = iPlantRepository.findById(id).orElseThrow(() -> new RuntimeException("Planta no encontrada."));

        if (nombre != null) plant.setNombre(nombre);
        if (paisId != null) {
            Country pais = countryRepository.findById(paisId).orElseThrow(() -> new RuntimeException("Pais no encontrado."));
            plant.setPais(pais);
        }
        if (cantidadLecturas != null) plant.setCantidadLecturas(cantidadLecturas);
        if (alertasMedias != null) plant.setAlertasMedias(alertasMedias);
        if (alertasRojas != null) plant.setAlertasRojas(alertasRojas);

        return iPlantRepository.save(plant);
    }

    public void deletePlant(Long id) {
        iPlantRepository.deleteById(id);
    }
}
