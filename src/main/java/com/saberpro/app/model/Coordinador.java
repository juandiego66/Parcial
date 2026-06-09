package com.saberpro.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "coordinadores")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Coordinador extends Usuario {

    @Column(name = "area_asignada")
    private String areaAsignada;

    public Coordinador() { setRol(Rol.COORDINADOR); }

    public String getAreaAsignada() { return areaAsignada; }
    public void setAreaAsignada(String areaAsignada) { this.areaAsignada = areaAsignada; }
}