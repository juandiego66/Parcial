package com.saberpro.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saberpro.app.model.Estudiante;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByNumeroCedula(String numeroCedula);
    Optional<Estudiante> findByNumeroRegistro(String numeroRegistro);
    List<Estudiante> findByAprobadoSaberPro(Boolean aprobado);
    List<Estudiante> findByTipoPrograma(Estudiante.TipoPrograma tipoPrograma);
    List<Estudiante> findByPrograma(String programa);
    boolean existsByNumeroCedula(String numeroCedula);
    boolean existsByNumeroRegistro(String numeroRegistro);
    boolean existsByCorreo(String correo);
}