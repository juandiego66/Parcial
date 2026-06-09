package com.saberpro.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "resultados_saber_pro")
public class ResultadoSaberPro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "estudiante_id", unique = true)
    private Estudiante estudiante;

    @Column(name = "numero_registro")
    private String numeroRegistro;

    @Column(name = "puntaje_total")
    private Integer puntajeTotal;

    @Column(name = "puntaje_nivel")
    private String puntajeNivel;

    // Competencias genéricas
    @Column(name = "comunicacion_escrita")
    private Double comunicacionEscrita;
    @Column(name = "comunicacion_escrita_nivel")
    private String comunicacionEscritaNivel;

    @Column(name = "razonamiento_cuantitativo")
    private Double razonamientoCuantitativo;
    @Column(name = "razonamiento_cuantitativo_nivel")
    private String razonamientoCuantitativoNivel;

    @Column(name = "lectura_critica")
    private Double lecturaCritica;
    @Column(name = "lectura_critica_nivel")
    private String lecturaCriticaNivel;

    @Column(name = "competencias_ciudadanas")
    private Double competenciasCiudadanas;
    @Column(name = "competencias_ciudadanas_nivel")
    private String competenciasCiudadanasNivel;

    @Column(name = "ingles")
    private Double ingles;
    @Column(name = "ingles_nivel")
    private String inglesNivel;

    @Column(name = "nivel_ingles")
    private String nivelIngles; // A1, A2, B1, B2, etc.

    // Competencias específicas ingeniería
    @Column(name = "formulacion_proyectos")
    private Double formulacionProyectos;
    @Column(name = "formulacion_proyectos_nivel")
    private String formulacionProyectosNivel;

    @Column(name = "pensamiento_cientifico")
    private Double pensamientoCientifico;
    @Column(name = "pensamiento_cientifico_nivel")
    private String pensamientoCientificoNivel;

    @Column(name = "diseno_software")
    private Double disenoSoftware;
    @Column(name = "diseno_software_nivel")
    private String disenoSoftwareNivel;

    @Column(name = "tiene_beneficio")
    private Boolean tieneBeneficio = false;

    @Column(name = "tipo_beneficio")
    private String tipoBeneficio;
    
    @Column(name = "puede_recibir_titulo")
    private Boolean puedeRecibirTitulo = true;

    @Column(name = "descripcion_beneficio", length = 600)
    private String descripcionBeneficio;

    // Getters y Setters completos
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }

    public String getNumeroRegistro() { return numeroRegistro; }
    public void setNumeroRegistro(String numeroRegistro) { this.numeroRegistro = numeroRegistro; }

    public Integer getPuntajeTotal() { return puntajeTotal; }
    public void setPuntajeTotal(Integer puntajeTotal) { this.puntajeTotal = puntajeTotal; }

    public String getPuntajeNivel() { return puntajeNivel; }
    public void setPuntajeNivel(String puntajeNivel) { this.puntajeNivel = puntajeNivel; }

    public Double getComunicacionEscrita() { return comunicacionEscrita; }
    public void setComunicacionEscrita(Double v) { this.comunicacionEscrita = v; }

    public String getComunicacionEscritaNivel() { return comunicacionEscritaNivel; }
    public void setComunicacionEscritaNivel(String v) { this.comunicacionEscritaNivel = v; }

    public Double getRazonamientoCuantitativo() { return razonamientoCuantitativo; }
    public void setRazonamientoCuantitativo(Double v) { this.razonamientoCuantitativo = v; }

    public String getRazonamientoCuantitativoNivel() { return razonamientoCuantitativoNivel; }
    public void setRazonamientoCuantitativoNivel(String v) { this.razonamientoCuantitativoNivel = v; }

    public Double getLecturaCritica() { return lecturaCritica; }
    public void setLecturaCritica(Double v) { this.lecturaCritica = v; }

    public String getLecturaCriticaNivel() { return lecturaCriticaNivel; }
    public void setLecturaCriticaNivel(String v) { this.lecturaCriticaNivel = v; }

    public Double getCompetenciasCiudadanas() { return competenciasCiudadanas; }
    public void setCompetenciasCiudadanas(Double v) { this.competenciasCiudadanas = v; }

    public String getCompetenciasCiudadanasNivel() { return competenciasCiudadanasNivel; }
    public void setCompetenciasCiudadanasNivel(String v) { this.competenciasCiudadanasNivel = v; }

    public Double getIngles() { return ingles; }
    public void setIngles(Double v) { this.ingles = v; }

    public String getInglesNivel() { return inglesNivel; }
    public void setInglesNivel(String v) { this.inglesNivel = v; }

    public String getNivelIngles() { return nivelIngles; }
    public void setNivelIngles(String v) { this.nivelIngles = v; }

    public Double getFormulacionProyectos() { return formulacionProyectos; }
    public void setFormulacionProyectos(Double v) { this.formulacionProyectos = v; }

    public String getFormulacionProyectosNivel() { return formulacionProyectosNivel; }
    public void setFormulacionProyectosNivel(String v) { this.formulacionProyectosNivel = v; }

    public Double getPensamientoCientifico() { return pensamientoCientifico; }
    public void setPensamientoCientifico(Double v) { this.pensamientoCientifico = v; }

    public String getPensamientoCientificoNivel() { return pensamientoCientificoNivel; }
    public void setPensamientoCientificoNivel(String v) { this.pensamientoCientificoNivel = v; }

    public Double getDisenoSoftware() { return disenoSoftware; }
    public void setDisenoSoftware(Double v) { this.disenoSoftware = v; }

    public String getDisenoSoftwareNivel() { return disenoSoftwareNivel; }
    public void setDisenoSoftwareNivel(String v) { this.disenoSoftwareNivel = v; }

    public Boolean getTieneBeneficio() { return tieneBeneficio; }
    public void setTieneBeneficio(Boolean v) { this.tieneBeneficio = v; }

    public String getTipoBeneficio() { return tipoBeneficio; }
    public void setTipoBeneficio(String v) { this.tipoBeneficio = v; }
    
    public Boolean getPuedeRecibirTitulo() { return puedeRecibirTitulo; }
    public void setPuedeRecibirTitulo(Boolean v) { this.puedeRecibirTitulo = v; }

    public String getDescripcionBeneficio() { return descripcionBeneficio; }
    public void setDescripcionBeneficio(String v) { this.descripcionBeneficio = v; }
}