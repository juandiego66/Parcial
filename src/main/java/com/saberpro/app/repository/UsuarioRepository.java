package com.saberpro.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.saberpro.app.model.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByNumeroCedula(String numeroCedula);
    boolean existsByCorreo(String correo);
    boolean existsByNumeroCedula(String numeroCedula);
}