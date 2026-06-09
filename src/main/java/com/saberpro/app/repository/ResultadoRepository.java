package com.saberpro.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.saberpro.app.model.ResultadoSaberPro;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResultadoRepository extends JpaRepository<ResultadoSaberPro, Long> {
    Optional<ResultadoSaberPro> findByEstudianteId(Long estudianteId);
    Optional<ResultadoSaberPro> findByNumeroRegistro(String numeroRegistro);
    List<ResultadoSaberPro> findByTieneBeneficio(Boolean tieneBeneficio);

    @Query("SELECT r FROM ResultadoSaberPro r WHERE r.estudiante.tipoPrograma = :tipo")
    List<ResultadoSaberPro> findByTipoPrograma(
        @org.springframework.data.repository.query.Param("tipo")
        com.saberpro.app.model.Estudiante.TipoPrograma tipo
    );
}