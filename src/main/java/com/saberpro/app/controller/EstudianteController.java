package com.saberpro.app.controller;

import com.saberpro.app.model.*;
import com.saberpro.app.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    private final EstudianteRepository estudianteRepository;
    private final ResultadoRepository  resultadoRepository;

    public EstudianteController(EstudianteRepository estudianteRepository,
                                ResultadoRepository resultadoRepository) {
        this.estudianteRepository = estudianteRepository;
        this.resultadoRepository  = resultadoRepository;
    }

    // ── verificación de sesión ──────────────────────────────────────────────
    private boolean esEstudiante(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u != null && u.getRol() == Usuario.Rol.ESTUDIANTE;
    }

    private Estudiante getEstudianteActual(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        return estudianteRepository.findByNumeroCedula(u.getNumeroCedula()).orElse(null);
    }

    // ── Dashboard ───────────────────────────────────────────────────────────
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!esEstudiante(session)) return "redirect:/login";

        Estudiante estudiante = getEstudianteActual(session);
        model.addAttribute("estudiante", estudiante);

        if (estudiante != null) {
            resultadoRepository.findByEstudianteId(estudiante.getId())
                .ifPresent(r -> model.addAttribute("resultado", r));
        }

        return "estudiante/dashboard";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  CARGAR PAGO SABER PRO
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/pago")
    public String pagoForm(HttpSession session, Model model) {
        if (!esEstudiante(session)) return "redirect:/login";
        Estudiante estudiante = getEstudianteActual(session);
        model.addAttribute("estudiante", estudiante);
        return "estudiante/pago";
    }

    @PostMapping("/pago/guardar")
    public String guardarPago(@RequestParam String comprobantePago,
                              HttpSession session,
                              RedirectAttributes ra) {
        if (!esEstudiante(session)) return "redirect:/login";

        Estudiante estudiante = getEstudianteActual(session);
        if (estudiante == null) return "redirect:/login";

        if (comprobantePago == null || comprobantePago.isBlank()) {
            ra.addFlashAttribute("error", "Debe ingresar el número de comprobante.");
            return "redirect:/estudiante/pago";
        }

        estudiante.setComprobantePago(comprobantePago);
        estudiante.setPagoSaberPro(true);
        estudianteRepository.save(estudiante);

        // Actualizar sesión
        session.setAttribute("usuario", estudiante);

        ra.addFlashAttribute("exito", "Comprobante de pago registrado correctamente.");
        return "redirect:/estudiante/dashboard";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  RESULTADO ÚNICO (mi resultado)
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/resultado")
    public String miResultado(HttpSession session, Model model) {
        if (!esEstudiante(session)) return "redirect:/login";

        Estudiante estudiante = getEstudianteActual(session);
        if (estudiante == null) return "redirect:/login";

        model.addAttribute("estudiante", estudiante);
        resultadoRepository.findByEstudianteId(estudiante.getId())
            .ifPresent(r -> model.addAttribute("resultado", r));

        return "estudiante/resultado";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  RESULTADOS TOTAL (todos los estudiantes)
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/resultados")
    public String resultadosTotal(HttpSession session, Model model) {
        if (!esEstudiante(session)) return "redirect:/login";
        model.addAttribute("resultados", resultadoRepository.findAll());
        model.addAttribute("estudiante", getEstudianteActual(session));
        return "estudiante/resultados";
    }

    // ════════════════════════════════════════════════════════════════════════
    //  RESOLUCIÓN
    // ════════════════════════════════════════════════════════════════════════

    @GetMapping("/resolucion")
    public String resolucion(HttpSession session, Model model) {
        if (!esEstudiante(session)) return "redirect:/login";
        model.addAttribute("estudiante", getEstudianteActual(session));
        return "estudiante/resolucion";
    }
}