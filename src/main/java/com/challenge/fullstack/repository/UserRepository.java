package com.challenge.fullstack.repository;

import com.challenge.fullstack.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

//@Repository
//public class UserRepository implements IUserRepository{

  //  @Autowired
    //private JdbcTemplate jdbcTemplate;

  //  @Override
 //   public UserModel findByName(String user) {
   //     String SQL = "SELECT * FROM users WHERE name = ?";
     //   return jdbcTemplate.queryForObject(SQL, new Object[]{user},
       //         new BeanPropertyRowMapper<>(UserModel.class));
   // }
  @Repository
  public interface UserRepository extends JpaRepository<UserModel, Long> {
      UserModel findByName(String name);
  }