package com.challenge.fullstack.model;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name = "plants")
public class PlantModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    private Integer cantidadLecturas;
    private Integer alertasMedias;
    private Integer alertasRojas;

    @Column(name = "readings_ok", nullable = false)
    private boolean readingsOk;

    // Agregar atributo "disabled" si es necesario
    private boolean disabled;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private UserModel createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    public PlantModel(){}

    public PlantModel(Long id, String nombre, Country country, Integer cantidadLecturas, Integer alertasMedias, Integer alertasRojas, boolean readingsOk, boolean disabled, UserModel createdBy, Date createdAt){
        this.id = id;
        this.nombre = nombre;
        this.country = country;
        this.cantidadLecturas = cantidadLecturas;
        this.alertasMedias = alertasMedias;
        this.alertasRojas = alertasRojas;
        this.readingsOk = readingsOk;
        this.disabled = disabled;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
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

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
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

    public UserModel getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserModel createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
