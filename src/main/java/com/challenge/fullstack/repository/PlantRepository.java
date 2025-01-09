package com.challenge.fullstack.repository;

import com.challenge.fullstack.model.PlantModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
//public class PlantRepository implements  IPlantRepository{

  //  @Autowired
    //private JdbcTemplate jdbcTemplate;

  //  @Override
//    public List<PlantModel> findAll() {
  //      String SQL = "SELECT * FROM plants";
    //    return  jdbcTemplate.query(SQL, BeanPropertyRowMapper.newInstance(PlantModel.class));
  //  }
//}
