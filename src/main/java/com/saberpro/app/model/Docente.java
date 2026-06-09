package com.saberpro.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "docentes")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Docente extends Usuario {

    @Column(name = "area_asignada")
    private String areaAsignada;

    @ManyToOne
    @JoinColumn(name = "facultad_id")
    private Facultad facultad;

    public Docente() { setRol(Rol.DOCENTE); }

    public String getAreaAsignada() { return areaAsignada; }
    public void setAreaAsignada(String areaAsignada) { this.areaAsignada = areaAsignada; }

    public Facultad getFacultad() { return facultad; }
    public void setFacultad(Facultad facultad) { this.facultad = facultad; }
}