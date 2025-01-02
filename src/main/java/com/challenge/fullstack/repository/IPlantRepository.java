package com.challenge.fullstack.repository;

import com.challenge.fullstack.model.PlantModel;

import java.util.List;

public interface IPlantRepository {
    public List<PlantModel> findAll();
}
