package com.challenge.fullstack.repository;

import com.challenge.fullstack.model.PlantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlantRepository extends JpaRepository<PlantModel, Long> {

    @Query("SELECT COUNT(p) FROM PlantModel p WHERE p.readingsOk = true")
    int countByReadingsOk();

    @Query("SELECT COUNT(p) FROM PlantModel p WHERE p.mediumAlerts > 0")
    int countByMediumAlerts();

    @Query("SELECT COUNT(p) FROM PlantModel p WHERE p.redAlerts > 0")
    int countByRedAlerts();

    @Query("SELECT COUNT(p) FROM PlantModel p WHERE p.disabled = true")
    int countByDisabledSensors();
}
