package com.saberpro.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saberpro.app.model.Facultad;

import java.util.Optional;

@Repository
public interface FacultadRepository extends JpaRepository<Facultad, Long> {
    Optional<Facultad> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}