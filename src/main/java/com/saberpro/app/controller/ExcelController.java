package com.saberpro.app.controller;

import com.saberpro.app.model.*;
import com.saberpro.app.repository.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/coordinador/excel")
public class ExcelController {

    private final EstudianteRepository estudianteRepository;

    public ExcelController(EstudianteRepository estudianteRepository) {
        this.estudianteRepository = estudianteRepository;
    }

    private boolean esCoordinador(HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        return u != null && u.getRol() == Usuario.Rol.COORDINADOR;
    }

    // ════════════════════════════════════════════════════════════════════════
    //  IMPORTAR desde Excel
    // ════════════════════════════════════════════════════════════════════════
    @PostMapping("/importar")
    public String importar(@RequestParam("archivo") MultipartFile archivo,
                           HttpSession session,
                           RedirectAttributes ra) {
        if (!esCoordinador(session)) return "redirect:/login";

        if (archivo.isEmpty()) {
            ra.addFlashAttribute("error", "Selecciona un archivo Excel.");
            return "redirect:/coordinador/estudiantes";
        }

        int creados  = 0;
        int omitidos = 0;
        int errores  = 0;

        try (Workbook wb = new XSSFWorkbook(archivo.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);

            // Fila 0 = encabezados, datos desde fila 1
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    String cedula    = celda(row, 0);
                    String nombre1   = celda(row, 1);
                    String nombre2   = celda(row, 2);
                    String apellido1 = celda(row, 3);
                    String apellido2 = celda(row, 4);
                    String correo    = celda(row, 5);
                    String telefono  = celda(row, 6);
                    String registro  = celda(row, 7);
                    String semestreStr = celda(row, 8);
                    String programa  = celda(row, 9);
                    String tipoStr   = celda(row, 10);

                    if (cedula.isBlank() || nombre1.isBlank() ||
                        apellido1.isBlank() || correo.isBlank()) {
                        omitidos++;
                        continue;
                    }

                    if (estudianteRepository.existsByNumeroCedula(cedula) ||
                        estudianteRepository.existsByCorreo(correo) ||
                        (!registro.isBlank() &&
                         estudianteRepository.existsByNumeroRegistro(registro))) {
                        omitidos++;
                        continue;
                    }

                    Estudiante est = new Estudiante();
                    est.setNumeroCedula(cedula);
                    est.setPrimerNombre(nombre1);
                    est.setSegundoNombre(nombre2.isBlank() ? null : nombre2);
                    est.setPrimerApellido(apellido1);
                    est.setSegundoApellido(apellido2.isBlank() ? null : apellido2);
                    est.setCorreo(correo);
                    est.setTelefono(telefono.isBlank() ? null : telefono);
                    est.setNumeroRegistro(registro.isBlank() ? null : registro);
                    est.setContrasena(cedula);
                    est.setRol(Usuario.Rol.ESTUDIANTE);
                    est.setPagoSaberPro(false);
                    est.setAprobadoSaberPro(false);

                    try {
                        est.setSemestre(Integer.parseInt(semestreStr));
                    } catch (NumberFormatException ignored) {
                        est.setSemestre(null);
                    }

                    est.setPrograma(programa.isBlank() ? null : programa);

                    Estudiante.TipoPrograma tipo =
                        tipoStr.equalsIgnoreCase("TECNOLOGIA") ||
                        tipoStr.equalsIgnoreCase("TYT") ||
                        tipoStr.equalsIgnoreCase("Tecnología")
                            ? Estudiante.TipoPrograma.TECNOLOGIA
                            : Estudiante.TipoPrograma.PROFESIONAL;
                    est.setTipoPrograma(tipo);

                    estudianteRepository.save(est);
                    creados++;

                } catch (Exception e) {
                    errores++;
                    System.out.println("Error fila " + (i + 1) + ": " + e.getMessage());
                }
            }

            ra.addFlashAttribute("exito",
                "Importación completada — Creados: " + creados +
                " | Omitidos (ya existen): " + omitidos +
                (errores > 0 ? " | Errores: " + errores : ""));

        } catch (IOException e) {
            ra.addFlashAttribute("error",
                "No se pudo leer el archivo. Asegúrate de que sea .xlsx");
        }

        return "redirect:/coordinador/estudiantes";
    }

    
    
    @GetMapping("/plantilla")
    public void plantilla(HttpServletResponse response,
                          HttpSession session) throws IOException {
        if (!esCoordinador(session)) {
            response.sendRedirect("/login");
            return;
        }

        response.setContentType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition",
            "attachment; filename=plantilla_estudiantes.xlsx");

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Estudiantes");

            CellStyle headerStyle = wb.createCellStyle();
            Font font = wb.createFont();
            font.setBold(true);
            font.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(font);
            headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // Nota informativa
            CellStyle noteStyle = wb.createCellStyle();
            Font noteFont = wb.createFont();
            noteFont.setItalic(true);
            noteFont.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
            noteStyle.setFont(noteFont);

            String[] headers = {
                "N° Cédula *", "Primer Nombre *", "Segundo Nombre",
                "Primer Apellido *", "Segundo Apellido", "Correo *",
                "Teléfono", "N° Registro", "Semestre",
                "Programa", "Tipo (PROFESIONAL / TECNOLOGIA)"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 6000);
            }

            // Fila de ejemplo
            Row ejemplo = sheet.createRow(1);
            String[] datos = {
                "1020300001", "Juan", "Carlos",
                "Pérez", "García", "juan.perez@correo.com",
                "3101234567", "EK20240001", "8",
                "Ingeniería de Sistemas", "PROFESIONAL"
            };
            for (int i = 0; i < datos.length; i++) {
                Cell cell = ejemplo.createCell(i);
                cell.setCellValue(datos[i]);
                cell.setCellStyle(noteStyle);
            }

            wb.write(response.getOutputStream());
        }
    }
    // ════════════════════════════════════════════════════════════════════════
    //  EXPORTAR a Excel
    // ════════════════════════════════════════════════════════════════════════
    @GetMapping("/exportar")
    public void exportar(HttpServletResponse response,
                         HttpSession session) throws IOException {
        if (!esCoordinador(session)) {
            response.sendRedirect("/login");
            return;
        }

        response.setContentType(
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition",
            "attachment; filename=estudiantes_saberpro.xlsx");

        List<Estudiante> estudiantes = estudianteRepository.findAll();

        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("Estudiantes");

            // ── Estilos ──────────────────────────────────────────────────
            CellStyle headerStyle = wb.createCellStyle();
            Font headerFont = wb.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.ROYAL_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);

            CellStyle dataStyle = wb.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);

            CellStyle altStyle = wb.createCellStyle();
            altStyle.cloneStyleFrom(dataStyle);
            altStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            altStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle badgeOk = wb.createCellStyle();
            badgeOk.cloneStyleFrom(dataStyle);
            badgeOk.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            badgeOk.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle badgeNo = wb.createCellStyle();
            badgeNo.cloneStyleFrom(dataStyle);
            badgeNo.setFillForegroundColor(IndexedColors.ROSE.getIndex());
            badgeNo.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            // ── Encabezados ──────────────────────────────────────────────
            String[] headers = {
                "N° Cédula", "Primer Nombre", "Segundo Nombre",
                "Primer Apellido", "Segundo Apellido", "Correo",
                "Teléfono", "N° Registro", "Semestre",
                "Programa", "Tipo Programa", "Pago", "Aprobado"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 5000);
            }
            headerRow.setHeightInPoints(20);

            // ── Datos ────────────────────────────────────────────────────
            int rowNum = 1;
            for (Estudiante e : estudiantes) {
                Row row = sheet.createRow(rowNum);
                row.setHeightInPoints(16);

                CellStyle base = (rowNum % 2 == 0) ? altStyle : dataStyle;

                setCelda(row, 0,  e.getNumeroCedula(), base);
                setCelda(row, 1,  e.getPrimerNombre(), base);
                setCelda(row, 2,  e.getSegundoNombre() != null ?
                    e.getSegundoNombre() : "", base);
                setCelda(row, 3,  e.getPrimerApellido(), base);
                setCelda(row, 4,  e.getSegundoApellido() != null ?
                    e.getSegundoApellido() : "", base);
                setCelda(row, 5,  e.getCorreo(), base);
                setCelda(row, 6,  e.getTelefono() != null ?
                    e.getTelefono() : "", base);
                setCelda(row, 7,  e.getNumeroRegistro() != null ?
                    e.getNumeroRegistro() : "", base);
                setCelda(row, 8,  e.getSemestre() != null ?
                    String.valueOf(e.getSemestre()) : "", base);
                setCelda(row, 9,  e.getPrograma() != null ?
                    e.getPrograma() : "", base);
                setCelda(row, 10, e.getTipoPrograma() != null ?
                    e.getTipoPrograma().name() : "", base);

                // Pago con color
                Cell pagoCel = row.createCell(11);
                pagoCel.setCellValue(e.getPagoSaberPro() ? "Aceptado" : "Pendiente");
                pagoCel.setCellStyle(e.getPagoSaberPro() ? badgeOk : badgeNo);

                // Aprobado con color
                Cell aprobCel = row.createCell(12);
                aprobCel.setCellValue(e.getAprobadoSaberPro() ? "Sí" : "No");
                aprobCel.setCellStyle(e.getAprobadoSaberPro() ? badgeOk : badgeNo);

                rowNum++;
            }

            // Ajuste automático de columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            wb.write(response.getOutputStream());
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    private String celda(Row row, int col) {
        Cell cell = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING  -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                double v = cell.getNumericCellValue();
                yield v == Math.floor(v)
                    ? String.valueOf((long) v)
                    : String.valueOf(v);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default      -> "";
        };
    }

    private void setCelda(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }
}