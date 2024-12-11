package br.usp.projetoa.util;

import java.time.LocalDate;

public class SemestreUtil {
    public static String determinaSemestreAnoAtual() {
        LocalDate dataAtual = LocalDate.now();
        int anoAtual = dataAtual.getYear();
        int mesAtual = dataAtual.getMonthValue();

        String semestre;
        if (mesAtual >= 2 && mesAtual <= 7) {
            semestre = "1S";
        } else {
            semestre = "2S";
        }

        return semestre + anoAtual;
    }

    public static LocalDate getPrazoEntregaRelatorio() {
        LocalDate dataAtual = LocalDate.now();
        int anoAtual = dataAtual.getYear();
        int mesAtual = dataAtual.getMonthValue();

        if (mesAtual >= 2 && mesAtual <= 7) {
            return LocalDate.of(anoAtual, 8, 30); // Prazo: 30 de agosto do ano atual
        }

        if (mesAtual == 1) {
            return LocalDate.of(anoAtual, 3, 30); // Prazo: 30 de março do ano atual
        }

        return LocalDate.of(anoAtual + 1, 3, 30); // Prazo: 30 de março do ano seguinte
    }
}
