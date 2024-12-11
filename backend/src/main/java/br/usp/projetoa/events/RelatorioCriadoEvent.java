package br.usp.projetoa.events;

import br.usp.projetoa.model.Relatorio;

public class RelatorioCriadoEvent {
    private final Relatorio relatorio;

    public RelatorioCriadoEvent(Relatorio relatorio) {
        this.relatorio = relatorio;
    }

    public Relatorio getRelatorio() {
        return relatorio;
    }
}
