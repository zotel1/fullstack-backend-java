package com.challenge.fullstack.repository;

import com.challenge.fullstack.model.PlantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPlantRepository extends JpaRepository<PlantModel, Long> {
}
