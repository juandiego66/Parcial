package com.saberpro.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "estudiantes")
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Estudiante extends Usuario {

    @Column(name = "numero_registro", unique = true)
    private String numeroRegistro;

    @Column(name = "semestre")
    private Integer semestre;

    @Column(name = "programa")
    private String programa;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_programa")
    private TipoPrograma tipoPrograma;

    @Column(name = "aprobado_saber_pro")
    private Boolean aprobadoSaberPro = false;

    @Column(name = "pago_saber_pro")
    private Boolean pagoSaberPro = false;

    @Column(name = "comprobante_pago")
    private String comprobantePago;

    public enum TipoPrograma {
        PROFESIONAL, TECNOLOGIA
    }

    public Estudiante() { setRol(Rol.ESTUDIANTE); }

    // Getters y Setters
    public String getNumeroRegistro() { return numeroRegistro; }
    public void setNumeroRegistro(String numeroRegistro) { this.numeroRegistro = numeroRegistro; }

    public Integer getSemestre() { return semestre; }
    public void setSemestre(Integer semestre) { this.semestre = semestre; }

    public String getPrograma() { return programa; }
    public void setPrograma(String programa) { this.programa = programa; }

    public TipoPrograma getTipoPrograma() { return tipoPrograma; }
    public void setTipoPrograma(TipoPrograma tipoPrograma) { this.tipoPrograma = tipoPrograma; }

    public Boolean getAprobadoSaberPro() { return aprobadoSaberPro; }
    public void setAprobadoSaberPro(Boolean aprobadoSaberPro) { this.aprobadoSaberPro = aprobadoSaberPro; }

    public Boolean getPagoSaberPro() { return pagoSaberPro; }
    public void setPagoSaberPro(Boolean pagoSaberPro) { this.pagoSaberPro = pagoSaberPro; }

    public String getComprobantePago() { return comprobantePago; }
    public void setComprobantePago(String comprobantePago) { this.comprobantePago = comprobantePago; }
}