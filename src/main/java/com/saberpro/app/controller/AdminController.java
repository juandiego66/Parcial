package com.saberpro.app.controller;

import com.saberpro.app.model.*;
import com.saberpro.app.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final DocenteRepository docenteRepository;
    private final CoordinadorRepository coordinadorRepository;
    private final FacultadRepository facultadRepository;

    public AdminController(UsuarioRepository usuarioRepository,
                           DocenteRepository docenteRepository,
                           CoordinadorRepository coordinadorRepository,
                           FacultadRepository facultadRepository) {
        this.usuarioRepository    = usuarioRepository;
        this.docenteRepository    = docenteRepository;
        this.coordinadorRepository = coordinadorRepository;
        this.facultadRepository   = facultadRepository;
    }

    // ── verificación de sesión ──────────────────────────────────────────────
    private boolean esAdmin(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u != null && u.getRol() == Usuario.Rol.ADMINISTRADOR;
    }

    // ── Dashboard ───────────────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        model.addAttribute("usuario", session.getAttribute("usuario"));
        model.addAttribute("totalDocentes",      docenteRepository.count());
        model.addAttribute("totalCoordinadores", coordinadorRepository.count());
        model.addAttribute("totalFacultades",    facultadRepository.count());
        return "admin/dashboard";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CRUD DOCENTES
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/docentes")
    public String listarDocentes(HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        model.addAttribute("docentes",   docenteRepository.findAll());
        model.addAttribute("facultades", facultadRepository.findAll());
        model.addAttribute("docente",    new Docente());
        return "admin/docentes";
    }

    @PostMapping("/docentes/guardar")
    public String guardarDocente(@Valid @ModelAttribute("docente") Docente docente,
                                 BindingResult result,
                                 HttpSession session,
                                 RedirectAttributes ra,
                                 Model model) {
        if (!esAdmin(session)) return "redirect:/login";

        if (docente.getId() == null) {
            if (docenteRepository.existsByNumeroCedula(docente.getNumeroCedula())) {
                ra.addFlashAttribute("error", "Ya existe un docente con esa cédula.");
                return "redirect:/admin/docentes";
            }
            if (docenteRepository.existsByCorreo(docente.getCorreo())) {
                ra.addFlashAttribute("error", "Ya existe un docente con ese correo.");
                return "redirect:/admin/docentes";
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("docentes",   docenteRepository.findAll());
            model.addAttribute("facultades", facultadRepository.findAll());
            return "admin/docentes";
        }

        docenteRepository.save(docente);
        ra.addFlashAttribute("exito", "Docente guardado correctamente.");
        return "redirect:/admin/docentes";
    }

    @GetMapping("/docentes/editar/{id}")
    public String editarDocente(@PathVariable Long id,
                                HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        model.addAttribute("docente",    docenteRepository.findById(id).orElseThrow());
        model.addAttribute("docentes",   docenteRepository.findAll());
        model.addAttribute("facultades", facultadRepository.findAll());
        return "admin/docentes";
    }

    @GetMapping("/docentes/eliminar/{id}")
    public String eliminarDocente(@PathVariable Long id,
                                  HttpSession session,
                                  RedirectAttributes ra) {
        if (!esAdmin(session)) return "redirect:/login";
        docenteRepository.deleteById(id);
        ra.addFlashAttribute("exito", "Docente eliminado correctamente.");
        return "redirect:/admin/docentes";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CRUD COORDINADORES
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/coordinadores")
    public String listarCoordinadores(HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        model.addAttribute("coordinadores", coordinadorRepository.findAll());
        model.addAttribute("coordinador",   new Coordinador());
        return "admin/coordinadores";
    }

    @PostMapping("/coordinadores/guardar")
    public String guardarCoordinador(@Valid @ModelAttribute("coordinador") Coordinador coordinador,
                                     BindingResult result,
                                     HttpSession session,
                                     RedirectAttributes ra,
                                     Model model) {
        if (!esAdmin(session)) return "redirect:/login";

        if (coordinador.getId() == null) {
            if (coordinadorRepository.existsByNumeroCedula(coordinador.getNumeroCedula())) {
                ra.addFlashAttribute("error", "Ya existe un coordinador con esa cédula.");
                return "redirect:/admin/coordinadores";
            }
            if (coordinadorRepository.existsByCorreo(coordinador.getCorreo())) {
                ra.addFlashAttribute("error", "Ya existe un coordinador con ese correo.");
                return "redirect:/admin/coordinadores";
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("coordinadores", coordinadorRepository.findAll());
            return "admin/coordinadores";
        }

        coordinadorRepository.save(coordinador);
        ra.addFlashAttribute("exito", "Coordinador guardado correctamente.");
        return "redirect:/admin/coordinadores";
    }

    @GetMapping("/coordinadores/editar/{id}")
    public String editarCoordinador(@PathVariable Long id,
                                    HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        model.addAttribute("coordinador",   coordinadorRepository.findById(id).orElseThrow());
        model.addAttribute("coordinadores", coordinadorRepository.findAll());
        return "admin/coordinadores";
    }

    @GetMapping("/coordinadores/eliminar/{id}")
    public String eliminarCoordinador(@PathVariable Long id,
                                      HttpSession session,
                                      RedirectAttributes ra) {
        if (!esAdmin(session)) return "redirect:/login";
        coordinadorRepository.deleteById(id);
        ra.addFlashAttribute("exito", "Coordinador eliminado correctamente.");
        return "redirect:/admin/coordinadores";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CRUD FACULTADES
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/facultades")
    public String listarFacultades(HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        model.addAttribute("facultades", facultadRepository.findAll());
        model.addAttribute("facultad",   new Facultad());
        return "admin/facultades";
    }

    @PostMapping("/facultades/guardar")
    public String guardarFacultad(@Valid @ModelAttribute("facultad") Facultad facultad,
                                  BindingResult result,
                                  HttpSession session,
                                  RedirectAttributes ra,
                                  Model model) {
        if (!esAdmin(session)) return "redirect:/login";

        if (facultad.getId() == null &&
            facultadRepository.existsByNombre(facultad.getNombre())) {
            ra.addFlashAttribute("error", "Ya existe una facultad con ese nombre.");
            return "redirect:/admin/facultades";
        }

        if (result.hasErrors()) {
            model.addAttribute("facultades", facultadRepository.findAll());
            return "admin/facultades";
        }

        facultadRepository.save(facultad);
        ra.addFlashAttribute("exito", "Facultad guardada correctamente.");
        return "redirect:/admin/facultades";
    }

    @GetMapping("/facultades/editar/{id}")
    public String editarFacultad(@PathVariable Long id,
                                 HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        model.addAttribute("facultad",   facultadRepository.findById(id).orElseThrow());
        model.addAttribute("facultades", facultadRepository.findAll());
        return "admin/facultades";
    }

    @GetMapping("/facultades/eliminar/{id}")
    public String eliminarFacultad(@PathVariable Long id,
                                   HttpSession session,
                                   RedirectAttributes ra) {
        if (!esAdmin(session)) return "redirect:/login";
        facultadRepository.deleteById(id);
        ra.addFlashAttribute("exito", "Facultad eliminada correctamente.");
        return "redirect:/admin/facultades";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  RESOLUCIÓN BENEFICIOS (página estática informativa)
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/resolucion")
    public String resolucion(HttpSession session, Model model) {
        if (!esAdmin(session)) return "redirect:/login";
        model.addAttribute("usuario", session.getAttribute("usuario"));
        return "admin/resolucion";
    }
}