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

    @Autowired
    private IPlantRepository iPlantRepository;

    @Autowired
    private CountryRepository countryRepository;

    private final RestTemplate restTemplate;

    @Autowired
    private CountryApiService countryApiService;

    private static final String REST_COUNTRIES_API = "https://restcountries.com/v3.1/all";


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

            try {
                // Llamada a la API externa
                String response = restTemplate.getForObject(REST_COUNTRIES_API, String.class);

                // Usar Jackson para deserializar
                ObjectMapper objectMapper = new ObjectMapper();
                List<CountryDto> countries = objectMapper.readValue(response, new TypeReference<List<CountryDto>>() {});

                // Buscar el país en la respuesta
                Optional<CountryDto> matchingCountry = countries.stream()
                        .filter(dto -> dto.getName().getCommon().equalsIgnoreCase(countryName))
                        .findFirst();

                if (matchingCountry.isEmpty()) {
                    throw new RuntimeException("País no encontrado en la API externa: " + countryName);
                }

                CountryDto dto = matchingCountry.get();
                System.out.println("País encontrado en la API externa: " + dto.getName().getCommon());

                // Guardar el país en la base de datos
                Country newCountry = new Country();
                newCountry.setName(dto.getName().getCommon());
                newCountry.setFlagUrl(dto.getFlags().getPng());
                return countryRepository.save(newCountry);

            } catch (Exception e) {
                throw new RuntimeException("Error al consumir o procesar la API externa: " + e.getMessage());
            }
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
