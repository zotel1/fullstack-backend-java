package com.challenge.fullstack.service;

import com.challenge.fullstack.dto.CountryDto;
import com.challenge.fullstack.dto.PlantDto;
import com.challenge.fullstack.dto.RestCountryDto;
import com.challenge.fullstack.model.Country;
import com.challenge.fullstack.model.PlantModel;
import com.challenge.fullstack.repository.CountryRepository;
import com.challenge.fullstack.repository.IPlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class PlantService {

    @Autowired
    private IPlantRepository iPlantRepository;

    @Autowired
    private CountryRepository countryRepository;

    private final RestTemplate restTemplate;

    @Autowired
    private CountryApiService countryApiService;


    @Autowired
    public PlantService(IPlantRepository iPlantRepository, CountryRepository countryRepository, RestTemplate restTemplate) {
        this.iPlantRepository = iPlantRepository;
        this.countryRepository = countryRepository;
        this.restTemplate = restTemplate;
    }

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
                        plant.getCountry().getName(),
                        plant.getCountry().getFlagUrl(),
                        plant.getCantidadLecturas(),
                        plant.getAlertasMedias(),
                        plant.getAlertasRojas()
                ))
                .collect(Collectors.toList());
    }

    public PlantModel createPlant(String nombre, String countryName) {
        // Verificar si el país ya existe en la base de datos
        Country country = countryRepository.findByName(countryName).orElseGet(() -> {
            System.out.println("País no encontrado en la base de datos. Consultando API externa...");

            // Consumir la API de Rest Country para obtener datos del país
            List<RestCountryDto> countries = countryApiService.fetchCountriesFromApi();

            // Buscar el país por su nombre común
            RestCountryDto matchingCountry = countries.stream()
                    .filter(dto -> dto.getName().getCommon().equalsIgnoreCase(countryName))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("País no encontrado en la API externa"));

            // Guardar el país en la base de datos
            Country newCountry = new Country();
            newCountry.setName(matchingCountry.getName().getCommon());
            newCountry.setFlagUrl(matchingCountry.getFlags().getPng());
            return countryRepository.save(newCountry);
        });

        System.out.println("País asociado: " + country.getName());

        // Crear la planta
        PlantModel plant = new PlantModel();
        plant.setNombre(nombre);
        plant.setCountry(country);
        plant.setCantidadLecturas((int) (Math.random() * 100)); // Generación aleatoria
        plant.setAlertasMedias((int) (Math.random() * 50));     // Generación aleatoria
        plant.setAlertasRojas((int) (Math.random() * 20));      // Generación aleatoria
        return iPlantRepository.save(plant);
    }


    public PlantModel updatePlant(Long id, String nombre, Long countryId, Integer cantidadLecturas, Integer alertasMedias, Integer alertasRojas) {
        PlantModel plant = iPlantRepository.findById(id).orElseThrow(() -> new RuntimeException("Planta no encontrada."));

        if (nombre != null) plant.setNombre(nombre);
        if (countryId != null) {
            Country country = countryRepository.findById(countryId).orElseThrow(() -> new RuntimeException("Pais no encontrado."));
            plant.setCountry(country);
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
