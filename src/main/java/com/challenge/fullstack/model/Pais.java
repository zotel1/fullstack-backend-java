package com.challenge.fullstack.model;

import javax.persistence.Entity;

import jakarta.persistence.GeneratedValue;
import lombok.Data;


@Data
@Entity
public class Pais {
    @Id
    @GeneratedValue

}
