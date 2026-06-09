package com.saberpro.app.controller;

import com.saberpro.app.model.Usuario;
import com.saberpro.app.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class AuthController {

    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(HttpSession session) {
        if (session.getAttribute("usuario") != null) {
            return redirectByRol((Usuario) session.getAttribute("usuario"));
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String correo,
                        @RequestParam String contrasena,
                        HttpSession session,
                        Model model) {

        Optional<Usuario> opt = usuarioRepository.findByCorreo(correo);

        if (opt.isEmpty() || !opt.get().getContrasena().equals(contrasena)) {
            model.addAttribute("error", "Correo o contraseña incorrectos.");
            return "login";
        }

        Usuario usuario = opt.get();
        session.setAttribute("usuario", usuario);
        return redirectByRol(usuario);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    private String redirectByRol(Usuario usuario) {
        return switch (usuario.getRol()) {
            case ADMINISTRADOR -> "redirect:/admin/dashboard";
            case COORDINADOR   -> "redirect:/coordinador/dashboard";
            case DOCENTE       -> "redirect:/docente/dashboard";
            case ESTUDIANTE    -> "redirect:/estudiante/dashboard";
        };
    }
}