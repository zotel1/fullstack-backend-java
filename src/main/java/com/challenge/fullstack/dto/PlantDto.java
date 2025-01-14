package com.challenge.fullstack.dto;

import com.challenge.fullstack.model.PlantModel;

public class PlantDto {
    private Long id;
    private String nombre;
    private String countryName;
    private String countryFlagUrl;
    private Integer cantidadLecturas;
    private Integer alertasMedias;
    private Integer alertasRojas;

    public PlantDto(Long id, String nombre, String countryName, String countryFlagUrl, Integer cantidadLecturas, Integer alertasMedias, Integer alertasRojas) {
        this.id = id;
        this.nombre = nombre;
        this.countryName = countryName;
        this.countryFlagUrl = countryFlagUrl;
        this.cantidadLecturas = cantidadLecturas;
        this.alertasMedias = alertasMedias;
        this.alertasRojas = alertasRojas;
    }

    public PlantDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryFlagUrl() {
        return countryFlagUrl;
    }

    public void setPaisFlagUrl(String countryFlagUrl) {
        this.countryFlagUrl = countryFlagUrl;
    }

    public Integer getCantidadLecturas() {
        return cantidadLecturas;
    }

    public void setCantidadLecturas(Integer cantidadLecturas) {
        this.cantidadLecturas = cantidadLecturas;
    }

    public Integer getAlertasMedias() {
        return alertasMedias;
    }

    public void setAlertasMedias(Integer alertasMedias) {
        this.alertasMedias = alertasMedias;
    }

    public Integer getAlertasRojas() {
        return alertasRojas;
    }

    public void setAlertasRojas(Integer alertasRojas) {
        this.alertasRojas = alertasRojas;
    }
}
