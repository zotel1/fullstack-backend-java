package com.challenge.fullstack.repository;

import com.challenge.fullstack.model.PlantModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    // Añadimos los metodos para filtrar las plantas por el usuario creador
    @Query("SELECT p FROM PlantModel p WHERE p.createdBy.user_id = :userId")
    List<PlantModel> findByCreatedBy(@Param("userId") Long userId);
// Permitimos a los administradores ver todas las plantas
    @Query("SELECT p FROM PlantModel p WHERE p.createdBy.user_id = :userId OR :isAdmin = true")
    List<PlantModel> findPlantsForUser(@Param("userId") Long userId, @Param("isAdmin") boolean isAdmin);

    //Este metodo permite obtener las plantas paginadas con Pageable de Spring Data JPA
    @Query("SELECT p FROM PlantModel p WHERE p.createdBy.user_id = :userId OR :isAdmin = true")
    Page<PlantModel> findPlantsForUserPaginated(@Param("userId") Long userId, @Param("isAdmin") boolean isAdmin, Pageable pageable);
}
