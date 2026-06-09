package com.saberpro.app.controller;

import com.saberpro.app.model.*;
import com.saberpro.app.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/docente")
public class DocenteController {

    private final DocenteRepository docenteRepository;
    private final EstudianteRepository estudianteRepository;
    private final ResultadoRepository resultadoRepository;
    private final FacultadRepository facultadRepository;

    public DocenteController(DocenteRepository docenteRepository,
                             EstudianteRepository estudianteRepository,
                             ResultadoRepository resultadoRepository,
                             FacultadRepository facultadRepository) {
        this.docenteRepository    = docenteRepository;
        this.estudianteRepository = estudianteRepository;
        this.resultadoRepository  = resultadoRepository;
        this.facultadRepository   = facultadRepository;
    }

    // ── verificación de sesión ──────────────────────────────────────────────
    private boolean esDocente(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u != null && u.getRol() == Usuario.Rol.DOCENTE;
    }

    private Docente getDocenteActual(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        return docenteRepository.findByNumeroCedula(u.getNumeroCedula()).orElse(null);
    }

    // ── Dashboard ───────────────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!esDocente(session)) return "redirect:/login";
        Docente docente = getDocenteActual(session);
        model.addAttribute("usuario",         docente);
        model.addAttribute("totalEstudiantes", estudianteRepository.count());
        model.addAttribute("totalResultados",  resultadoRepository.count());
        model.addAttribute("totalBeneficios",  resultadoRepository.findByTieneBeneficio(true).size());

        if (docente != null && docente.getFacultad() != null) {
            model.addAttribute("facultad", docente.getFacultad().getNombre());
            model.addAttribute("docentesPorFacultad",
                docenteRepository.findByFacultadId(docente.getFacultad().getId()).size());
        }
        return "docente/dashboard";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  DOCENTES POR FACULTAD Y POR CÉDULA
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/docentes")
    public String docentesPorFacultad(HttpSession session, Model model,
            @RequestParam(required = false) Long facultadId,
            @RequestParam(required = false) String cedula) {
        if (!esDocente(session)) return "redirect:/login";

        model.addAttribute("facultades", facultadRepository.findAll());

        if (cedula != null && !cedula.isBlank()) {
            docenteRepository.findByNumeroCedula(cedula).ifPresent(d ->
                model.addAttribute("docenteEncontrado", d));
            model.addAttribute("busquedaCedula", cedula);
        }

        if (facultadId != null) {
            model.addAttribute("docentes",          docenteRepository.findByFacultadId(facultadId));
            model.addAttribute("facultadSeleccionada", facultadId);
        } else {
            model.addAttribute("docentes", docenteRepository.findAll());
        }

        return "docente/docentes";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  RESULTADOS TOTAL Y ÚNICO
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/resultados")
    public String resultadosTotal(HttpSession session, Model model,
            @RequestParam(required = false) String cedula,
            @RequestParam(required = false) Long facultadId) {
        if (!esDocente(session)) return "redirect:/login";

        List<ResultadoSaberPro> resultados = resultadoRepository.findAll();

        // Filtro real por facultad usando TipoPrograma
        if (facultadId != null) {
            // Tecnología = id de facTec, Ingeniería = id de facIng
            // Buscamos el tipo según qué facultad seleccionaron
            facultadRepository.findById(facultadId).ifPresent(fac -> {
                Estudiante.TipoPrograma tipo =
                    fac.getNombre().equalsIgnoreCase("Tecnología")
                        ? Estudiante.TipoPrograma.TECNOLOGIA
                        : Estudiante.TipoPrograma.PROFESIONAL;
                model.addAttribute("resultados",
                    resultadoRepository.findByTipoPrograma(tipo));
            });
        } else {
            model.addAttribute("resultados", resultados);
        }

        if (cedula != null && !cedula.isBlank()) {
            estudianteRepository.findByNumeroCedula(cedula).ifPresent(est ->
                resultadoRepository.findByEstudianteId(est.getId()).ifPresent(r ->
                    model.addAttribute("resultadoBusqueda", r)));
            model.addAttribute("busquedaCedula", cedula);
        }

        model.addAttribute("facultades",          facultadRepository.findAll());
        model.addAttribute("facultadSeleccionada", facultadId);
        return "docente/resultados";
    }

    @GetMapping("/resultados/{id}")
    public String resultadoDetalle(@PathVariable Long id,
                                   HttpSession session, Model model) {
        if (!esDocente(session)) return "redirect:/login";
        model.addAttribute("resultado", resultadoRepository.findById(id).orElseThrow());
        return "docente/resultado-detalle";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  INFORME DE BENEFICIOS
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/beneficios")
    public String beneficios(HttpSession session, Model model) {
        if (!esDocente(session)) return "redirect:/login";
        model.addAttribute("conBeneficio",    resultadoRepository.findByTieneBeneficio(true));
        model.addAttribute("sinBeneficio",    resultadoRepository.findByTieneBeneficio(false));
        model.addAttribute("totalBeneficios", resultadoRepository.findByTieneBeneficio(true).size());
        return "docente/beneficios";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  RESOLUCIÓN
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/resolucion")
    public String resolucion(HttpSession session, Model model) {
        if (!esDocente(session)) return "redirect:/login";
        model.addAttribute("usuario", session.getAttribute("usuario"));
        return "docente/resolucion";
    }
}