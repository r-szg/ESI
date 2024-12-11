package br.usp.projetoa.events;

import br.usp.projetoa.model.Parecer;

public class ParecerCriadoEvent {
    private Parecer parecer;

    public ParecerCriadoEvent(Parecer parecer) {
        this.parecer = parecer;
    }

    public Parecer getParecer() {
        return parecer;
    }
}
