package com.challenge.fullstack.repository;

import com.challenge.fullstack.model.PlantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlantRepository extends JpaRepository<PlantModel, Long> {

    @Query("SELECT COUNT(p) FROM PlantModel p WHERE p.readingsOk = true")
    int countByReadingsOk();

    // Consulta corregida para alertasMedias (alertasMedias es el nombre en PlantModel)
    @Query("SELECT COUNT(p) FROM PlantModel p WHERE p.alertasMedias > 0")
    int countByMediumAlerts();

    // Consulta corregida para alertasRojas (alertasRojas es el nombre en PlantModel)
    @Query("SELECT COUNT(p) FROM PlantModel p WHERE p.alertasRojas > 0")
    int countByRedAlerts();

    // Esta consulta debe coincidir con el atributo en PlantModel. Si disabled no existe, elimínala o corrige.
    @Query("SELECT COUNT(p) FROM PlantModel p WHERE p.disabled = true")
    int countByDisabledSensors();
}
