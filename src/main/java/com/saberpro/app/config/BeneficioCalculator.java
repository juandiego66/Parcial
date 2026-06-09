package com.saberpro.app.config;

import com.saberpro.app.model.Estudiante;
import com.saberpro.app.model.ResultadoSaberPro;

public class BeneficioCalculator {

    public static void calcular(ResultadoSaberPro r, Estudiante.TipoPrograma tipo) {
        if (r.getPuntajeTotal() == null) {
            r.setTieneBeneficio(false);
            r.setTipoBeneficio("Sin beneficio");
            r.setPuedeRecibirTitulo(false);
            return;
        }

        int puntaje = r.getPuntajeTotal();

        if (tipo == Estudiante.TipoPrograma.TECNOLOGIA) {
            // TYT
            if (puntaje < 80) {
                r.setTieneBeneficio(false);
                r.setTipoBeneficio("Sin beneficio");
                r.setPuedeRecibirTitulo(false);
                r.setDescripcionBeneficio(
                    "Puntaje menor a 80. No puede recibir el título. " +
                    "Debe volver a presentar la prueba.");
            } else if (puntaje <= 150) {
                r.setTieneBeneficio(true);
                r.setPuedeRecibirTitulo(true);
                r.setTipoBeneficio("Exención informe/Seminario Grado II — Nota 4.5");
                r.setDescripcionBeneficio(
                    "Se exime la entrega del informe final del trabajo de grado, " +
                    "dándose concepto de aprobado; o se exonera realizar el espacio " +
                    "académico acreditable, Seminario de Grado II con nota de 4.5.");
            } else if (puntaje <= 170) {
                r.setTieneBeneficio(true);
                r.setPuedeRecibirTitulo(true);
                r.setTipoBeneficio("Exención informe/Seminario Grado II — Nota 4.7 + Beca 50% grado");
                r.setDescripcionBeneficio(
                    "Se exime la entrega del informe final del trabajo de grado, " +
                    "dándose concepto de aprobado; o se exonera realizar el espacio " +
                    "académico acreditable, Seminario de Grado II con nota de 4.7. " +
                    "Igualmente, se beca con el 50% el derecho pecuniario de grado.");
            } else {
                r.setTieneBeneficio(true);
                r.setPuedeRecibirTitulo(true);
                r.setTipoBeneficio("Exención informe/Seminario Grado II — Nota 5.0 + Beca 100% grado");
                r.setDescripcionBeneficio(
                    "Se exime la entrega del informe final del trabajo de grado, " +
                    "dándose concepto de aprobado; o se exonera realizar el espacio " +
                    "académico acreditable, Seminario de Grado II con nota de 5.0. " +
                    "Igualmente, se beca con el 100% el derecho pecuniario de grado.");
            }
        } else {
            // PROFESIONAL — Saber Pro
            if (puntaje < 120) {
                r.setTieneBeneficio(false);
                r.setTipoBeneficio("Sin beneficio");
                r.setPuedeRecibirTitulo(false);
                r.setDescripcionBeneficio(
                    "Puntaje menor a 120. No puede recibir el título. " +
                    "Debe volver a presentar la prueba.");
            } else if (puntaje <= 210) {
                r.setTieneBeneficio(true);
                r.setPuedeRecibirTitulo(true);
                r.setTipoBeneficio("Exención informe/Seminario Grado IV — Nota 4.5");
                r.setDescripcionBeneficio(
                    "Se exime la entrega del informe final del trabajo de grado, " +
                    "dándose concepto de aprobado; o se exonera realizar el espacio " +
                    "académico acreditable, Seminario de Grado IV con nota de 4.5.");
            } else if (puntaje <= 240) {
                r.setTieneBeneficio(true);
                r.setPuedeRecibirTitulo(true);
                r.setTipoBeneficio("Exención informe/Seminario Grado IV — Nota 4.7 + Beca 50% grado");
                r.setDescripcionBeneficio(
                    "Se exime la entrega del informe final del trabajo de grado, " +
                    "dándose concepto de aprobado; o se exonera realizar el espacio " +
                    "académico acreditable, Seminario de Grado IV con nota de 4.7. " +
                    "Igualmente, se beca con el 50% el derecho pecuniario de grado.");
            } else {
                r.setTieneBeneficio(true);
                r.setPuedeRecibirTitulo(true);
                r.setTipoBeneficio("Exención informe/Seminario Grado IV — Nota 5.0 + Beca 100% grado");
                r.setDescripcionBeneficio(
                    "Se exime la entrega del informe final del trabajo de grado, " +
                    "dándose concepto de aprobado; o se exonera realizar el espacio " +
                    "académico acreditable, Seminario de Grado IV con nota de 5.0. " +
                    "Igualmente, se beca con el 100% el derecho pecuniario de grado.");
            }
        }
    }
}