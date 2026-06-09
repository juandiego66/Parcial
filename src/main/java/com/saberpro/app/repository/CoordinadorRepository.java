package com.saberpro.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saberpro.app.model.Coordinador;

import java.util.Optional;

@Repository
public interface CoordinadorRepository extends JpaRepository<Coordinador, Long> {
    Optional<Coordinador> findByNumeroCedula(String numeroCedula);
    boolean existsByNumeroCedula(String numeroCedula);
    boolean existsByCorreo(String correo);
}