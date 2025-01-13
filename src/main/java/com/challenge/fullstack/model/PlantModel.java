package com.challenge.fullstack.model;

import jakarta.persistence.*;


@Entity
public class PlantModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "pais_id", nullable = false)
    private Country pais;

    private Integer cantidadLecturas;
    private Integer alertasMedias;
    private Integer alertasRojas;

    @Column(name = "readings_ok", nullable = false)
    private boolean readingsOk;

    // Agregar atributo "disabled" si es necesario
    private boolean disabled;

    public PlantModel(){

    }

    public PlantModel(Long id, String nombre, Country pais, Integer cantidadLecturas, Integer alertasMedias, Integer alertasRojas, boolean readingsOk, boolean disabled){
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.cantidadLecturas = cantidadLecturas;
        this.alertasMedias = alertasMedias;
        this.alertasRojas = alertasRojas;
        this.readingsOk = readingsOk;
        this.disabled = disabled;
    }

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

    public Country getPais() {
        return pais;
    }

    public void setPais(Country pais) {
        this.pais = pais;
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

    public boolean isReadingsOk() {
        return readingsOk;
    }



    public void setReadingsOk(boolean readingsOk) {
        this.readingsOk = readingsOk;
    }

    // NUEVO

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }


}
