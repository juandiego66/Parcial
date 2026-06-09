package com.saberpro.app.controller;

import com.saberpro.app.model.*;
import com.saberpro.app.config.*;
import com.saberpro.app.repository.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/coordinador")
public class CoordinadorController {

    private final EstudianteRepository estudianteRepository;
    private final ResultadoRepository  resultadoRepository;
    private final FacultadRepository   facultadRepository;

    public CoordinadorController(EstudianteRepository estudianteRepository,
                                 ResultadoRepository  resultadoRepository,
                                 FacultadRepository   facultadRepository) {
        this.estudianteRepository = estudianteRepository;
        this.resultadoRepository  = resultadoRepository;
        this.facultadRepository   = facultadRepository;
    }

    private boolean esCoordinador(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u != null && u.getRol() == Usuario.Rol.COORDINADOR;
    }

    // ── Dashboard ────────────────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!esCoordinador(session)) return "redirect:/login";
        model.addAttribute("usuario",          session.getAttribute("usuario"));
        model.addAttribute("totalEstudiantes", estudianteRepository.count());
        model.addAttribute("totalAprobados",   estudianteRepository.findByAprobadoSaberPro(true).size());
        model.addAttribute("totalResultados",  resultadoRepository.count());
        model.addAttribute("totalBeneficios",  resultadoRepository.findByTieneBeneficio(true).size());
        model.addAttribute("pendientesPago",
            estudianteRepository.findAll().stream()
                .filter(e -> !e.getPagoSaberPro()).count());
        return "coordinador/dashboard";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CRUD ESTUDIANTES
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/estudiantes")
    public String listarEstudiantes(HttpSession session, Model model) {
        if (!esCoordinador(session)) return "redirect:/login";
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        model.addAttribute("estudiante",  new Estudiante());
        return "coordinador/estudiantes";
    }

    @PostMapping("/estudiantes/guardar")
    public String guardarEstudiante(@ModelAttribute("estudiante") Estudiante estudiante,
                                    BindingResult result,
                                    HttpSession session,
                                    RedirectAttributes ra,
                                    Model model) {
        if (!esCoordinador(session)) return "redirect:/login";

        if (estudiante.getId() == null) {
            if (estudianteRepository.existsByNumeroCedula(estudiante.getNumeroCedula())) {
                ra.addFlashAttribute("error", "Ya existe un estudiante con esa cédula.");
                return "redirect:/coordinador/estudiantes";
            }
            if (estudianteRepository.existsByNumeroRegistro(estudiante.getNumeroRegistro())) {
                ra.addFlashAttribute("error", "Ya existe un estudiante con ese número de registro.");
                return "redirect:/coordinador/estudiantes";
            }
            if (estudianteRepository.existsByCorreo(estudiante.getCorreo())) {
                ra.addFlashAttribute("error", "Ya existe un estudiante con ese correo.");
                return "redirect:/coordinador/estudiantes";
            }
            // Asignar contraseña ANTES de validar
            estudiante.setContrasena(estudiante.getNumeroCedula());
        } else {
            Estudiante original = estudianteRepository.findById(estudiante.getId()).orElseThrow();
            estudiante.setContrasena(original.getContrasena());
            estudiante.setPagoSaberPro(original.getPagoSaberPro());
            estudiante.setComprobantePago(original.getComprobantePago());
            estudiante.setAprobadoSaberPro(original.getAprobadoSaberPro());
        }

        try {
            estudianteRepository.save(estudiante);
            ra.addFlashAttribute("exito", "Estudiante guardado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            ra.addFlashAttribute("error", "Error al guardar: " + e.getMessage());
        }

        return "redirect:/coordinador/estudiantes";
    }

    @GetMapping("/estudiantes/editar/{id}")
    public String editarEstudiante(@PathVariable Long id,
                                   HttpSession session, Model model) {
        if (!esCoordinador(session)) return "redirect:/login";
        model.addAttribute("estudiante",  estudianteRepository.findById(id).orElseThrow());
        model.addAttribute("estudiantes", estudianteRepository.findAll());
        return "coordinador/estudiantes";
    }

    @Transactional
    @GetMapping("/estudiantes/eliminar/{id}")
    public String eliminarEstudiante(@PathVariable Long id,
                                     HttpSession session,
                                     RedirectAttributes ra) {
        if (!esCoordinador(session)) return "redirect:/login";

        try {
            // Primero eliminar el resultado si existe
            resultadoRepository.findByEstudianteId(id)
                .ifPresent(r -> resultadoRepository.delete(r));

            // Luego eliminar el estudiante
            estudianteRepository.deleteById(id);

            ra.addFlashAttribute("exito", "Estudiante eliminado correctamente.");

        } catch (Exception e) {
            ra.addFlashAttribute("error",
                "No se pudo eliminar el estudiante: " + e.getMessage());
        }

        return "redirect:/coordinador/estudiantes";
    }

    // ── CALIFICAR (formulario de resultados desde CRUD estudiante) ──────────
    @GetMapping("/estudiantes/calificar/{id}")
    public String calificarForm(@PathVariable Long id,
                                HttpSession session, Model model) {
        if (!esCoordinador(session)) return "redirect:/login";

        Estudiante est = estudianteRepository.findById(id).orElseThrow();
        ResultadoSaberPro resultado = resultadoRepository
            .findByEstudianteId(id)
            .orElseGet(() -> {
                ResultadoSaberPro r = new ResultadoSaberPro();
                r.setEstudiante(est);
                r.setNumeroRegistro(est.getNumeroRegistro());
                return r;
            });

        model.addAttribute("estudiante", est);
        model.addAttribute("resultado",  resultado);
        return "coordinador/calificar";
    }

    @PostMapping("/estudiantes/calificar/guardar")
    public String calificarGuardar(@ModelAttribute("resultado") ResultadoSaberPro resultado,
                                   HttpSession session,
                                   RedirectAttributes ra) {
        if (!esCoordinador(session)) return "redirect:/login";

        // Obtener tipo de programa del estudiante
        Estudiante est = estudianteRepository
            .findById(resultado.getEstudiante().getId()).orElseThrow();

        BeneficioCalculator.calcular(resultado, est.getTipoPrograma());

        resultadoRepository.save(resultado);

        // Actualizar aprobación
        boolean aprobado = est.getTipoPrograma() == Estudiante.TipoPrograma.TECNOLOGIA
            ? resultado.getPuntajeTotal() >= 80
            : resultado.getPuntajeTotal() >= 120;
        est.setAprobadoSaberPro(aprobado);
        estudianteRepository.save(est);

        ra.addFlashAttribute("exito", "Resultados guardados correctamente.");
        return "redirect:/coordinador/estudiantes";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  PAGOS — ver quién pagó y aprobar
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/pagos")
    public String verPagos(HttpSession session, Model model) {
        if (!esCoordinador(session)) return "redirect:/login";

        List<Estudiante> todos = estudianteRepository.findAll();
        model.addAttribute("conPago",    todos.stream().filter(Estudiante::getPagoSaberPro).toList());
        model.addAttribute("sinPago",    todos.stream().filter(e -> !e.getPagoSaberPro()).toList());
        model.addAttribute("totalPago",  todos.stream().filter(Estudiante::getPagoSaberPro).count());
        model.addAttribute("totalSinPago", todos.stream().filter(e -> !e.getPagoSaberPro()).count());
        return "coordinador/pagos";
    }

 // Aprueba el PAGO (no al estudiante)
    @PostMapping("/pagos/aprobar/{id}")
    public String aprobarPago(@PathVariable Long id,
                               HttpSession session,
                               RedirectAttributes ra) {
        if (!esCoordinador(session)) return "redirect:/login";

        Estudiante est = estudianteRepository.findById(id).orElseThrow();
        est.setPagoSaberPro(true);
        estudianteRepository.save(est);

        ra.addFlashAttribute("exito",
            "Pago de " + est.getNombreCompleto() + " aceptado correctamente.");
        return "redirect:/coordinador/pagos";
    }
    // ════════════════════════════════════════════════════════════════════════
    //  RESULTADOS TOTAL con filtro por programa
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/resultados")
    public String resultadosTotal(HttpSession session, Model model,
            @RequestParam(required = false) String programa,
            @RequestParam(required = false) String facultad) {
        if (!esCoordinador(session)) return "redirect:/login";

        List<ResultadoSaberPro> todos = resultadoRepository.findAll();

        List<ResultadoSaberPro> filtrados = todos;

        // Filtro por facultad (TipoPrograma)
        if (facultad != null && !facultad.isBlank()) {
            Estudiante.TipoPrograma tipo = facultad.equalsIgnoreCase("Tecnología")
                ? Estudiante.TipoPrograma.TECNOLOGIA
                : Estudiante.TipoPrograma.PROFESIONAL;
            filtrados = filtrados.stream()
                .filter(r -> r.getEstudiante() != null &&
                             r.getEstudiante().getTipoPrograma() == tipo)
                .toList();
        }

        // Filtro por programa
        if (programa != null && !programa.isBlank()) {
            filtrados = filtrados.stream()
                .filter(r -> r.getEstudiante() != null &&
                             programa.equals(r.getEstudiante().getPrograma()))
                .toList();
        }

        model.addAttribute("resultados",           filtrados);
        model.addAttribute("programaSeleccionado", programa);
        model.addAttribute("facultadFiltro",       facultad);
        return "coordinador/resultados";
    }

    @GetMapping("/resultados/{id}")
    public String resultadoDetalle(@PathVariable Long id,
                                   HttpSession session, Model model) {
        if (!esCoordinador(session)) return "redirect:/login";
        model.addAttribute("resultado", resultadoRepository.findById(id).orElseThrow());
        return "coordinador/resultado-detalle";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  APROBAR (pantalla dedicada)
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/aprobar")
    public String aprobarEstudiantes(HttpSession session, Model model) {
        if (!esCoordinador(session)) return "redirect:/login";
        model.addAttribute("resultados", resultadoRepository.findAll());
        return "coordinador/aprobar";
    }

    @PostMapping("/aprobar/{estudianteId}")
    public String aprobarEstudiante(@PathVariable Long estudianteId,
                                    HttpSession session,
                                    RedirectAttributes ra) {
        if (!esCoordinador(session)) return "redirect:/login";

        Estudiante est = estudianteRepository.findById(estudianteId).orElseThrow();
        ResultadoSaberPro r = resultadoRepository
            .findByEstudianteId(estudianteId).orElse(null);

        if (r == null) {
            ra.addFlashAttribute("error", "El estudiante no tiene resultados cargados.");
            return "redirect:/coordinador/aprobar";
        }

        BeneficioCalculator.calcular(r, est.getTipoPrograma());
        resultadoRepository.save(r);

        boolean aprobado = est.getTipoPrograma() == Estudiante.TipoPrograma.TECNOLOGIA
            ? r.getPuntajeTotal() >= 80
            : r.getPuntajeTotal() >= 120;
        est.setAprobadoSaberPro(aprobado);
        estudianteRepository.save(est);

        ra.addFlashAttribute("exito", "Estado del estudiante actualizado.");
        return "redirect:/coordinador/aprobar";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  BENEFICIOS
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/beneficios")
    public String beneficios(HttpSession session, Model model) {
        if (!esCoordinador(session)) return "redirect:/login";
        model.addAttribute("conBeneficio",    resultadoRepository.findByTieneBeneficio(true));
        model.addAttribute("sinBeneficio",    resultadoRepository.findByTieneBeneficio(false));
        model.addAttribute("totalBeneficios", resultadoRepository.findByTieneBeneficio(true).size());
        return "coordinador/beneficios";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  RESOLUCIÓN
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/resolucion")
    public String resolucion(HttpSession session, Model model) {
        if (!esCoordinador(session)) return "redirect:/login";
        model.addAttribute("usuario", session.getAttribute("usuario"));
        return "coordinador/resolucion";
    }
}
