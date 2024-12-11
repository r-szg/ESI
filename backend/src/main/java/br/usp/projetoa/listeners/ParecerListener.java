package br.usp.projetoa.listeners;

import jakarta.mail.MessagingException;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.usp.projetoa.events.ParecerCriadoEvent;
import br.usp.projetoa.service.EmailService;

@Component
public class ParecerListener {
    private final EmailService emailService;

    public ParecerListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void handleParecerCriadoEvent(ParecerCriadoEvent event) {
        try {
            emailService.enviarEmail(
                event.getParecer().getRelatorio().getAluno().getEmail(), 
                "Parecer sobre o seu relatório", 
                "O docente " + event.getParecer().getParecista().getNomeCompleto()
                    + " (Nº USP: " + event.getParecer().getParecista().getNumeroUsp()
                    + ") avaliou seu relatório.\n\nPara acessar o parecer, por favor, acesse a"
                    + " plataforma.");
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
}
