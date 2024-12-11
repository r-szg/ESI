package br.usp.projetoa.listeners;

import jakarta.mail.MessagingException;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import br.usp.projetoa.events.RelatorioCriadoEvent;
import br.usp.projetoa.service.EmailService;

@Component
public class RelatorioListener {
    private final EmailService emailService;

    public RelatorioListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    @EventListener
    public void handleRelatorioCriadoEvent(RelatorioCriadoEvent event) {
        try {
            emailService.enviarEmail(
                event.getRelatorio().getDocente().getEmail(), 
                "Envio de relatório", 
                "O aluno " + event.getRelatorio().getAluno().getNomeCompleto() + " (Nº USP: " 
                    + event.getRelatorio().getAluno().getNumeroUsp() + ") enviou o relatório!"
                    + " \n\nPor gentileza, utilize a plataforma para visualizar e dar um parecer para o mesmo.");
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }
}
