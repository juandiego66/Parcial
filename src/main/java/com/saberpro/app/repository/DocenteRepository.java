package com.saberpro.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saberpro.app.model.Docente;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {
    List<Docente> findByFacultadId(Long facultadId);
    Optional<Docente> findByNumeroCedula(String numeroCedula);
    boolean existsByNumeroCedula(String numeroCedula);
    boolean existsByCorreo(String correo);
}