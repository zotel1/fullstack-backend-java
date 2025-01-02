package com.challenge.fullstack.service;

import com.challenge.fullstack.model.PlantModel;
import com.challenge.fullstack.repository.IPlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantService implements IPlantService {

    @Autowired
    private IPlantRepository iPlantRepository;

    @Override
    public List<PlantModel> findAll() {
        List<PlantModel> list;

        try {
            list = iPlantRepository.findAll();
        } catch (Exception ex) {
            throw ex;
        }

        return list;
    }
}