package br.usp.projetoa.listeners;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import jakarta.mail.MessagingException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import br.usp.projetoa.events.ParecerCriadoEvent;
import br.usp.projetoa.model.Aluno;
import br.usp.projetoa.model.Docente;
import br.usp.projetoa.model.Parecer;
import br.usp.projetoa.model.Relatorio;
import br.usp.projetoa.service.EmailService;

@SpringBootTest(classes = ParecerListener.class)
public class ParecerListenerTest {
    @MockBean private EmailService emailService;

    @Autowired private ApplicationEventPublisher eventPublisher;

    @Test
    @DisplayName("Deve capturar o evento e enviar um email com sucesso")
    void deveCapturarEventoEEnviarEmailComSucesso() throws MessagingException {
        Aluno aluno = new Aluno();
        aluno.setEmail("aluno@email.com");

        Docente docente = new Docente();
        docente.setNomeCompleto("docente");
        docente.setNumeroUsp(1L);

        Relatorio relatorio = new Relatorio();
        relatorio.setAluno(aluno);

        Parecer parecer = new Parecer();
        parecer.setParecista(docente);
        parecer.setRelatorio(relatorio);

        ParecerCriadoEvent event = new ParecerCriadoEvent(parecer);

        eventPublisher.publishEvent(event);

        verify(emailService, times(1)).enviarEmail(
            eq(aluno.getEmail()), 
            eq("Parecer sobre o seu relatório"), 
            contains(Long.toString(docente.getNumeroUsp()))
        );
    }

    @Test
    @DisplayName("Deve tratar exceção ao enviar email")
    void deveTratarExcecaoAoEnviarEmail() throws MessagingException {
        Aluno aluno = new Aluno();
        aluno.setEmail("aluno@email.com");

        Docente docente = new Docente();
        docente.setNomeCompleto("docente");
        docente.setNumeroUsp(1L);

        Relatorio relatorio = new Relatorio();
        relatorio.setAluno(aluno);

        Parecer parecer = new Parecer();
        parecer.setParecista(docente);
        parecer.setRelatorio(relatorio);

        ParecerCriadoEvent event = new ParecerCriadoEvent(parecer);

        doThrow(MessagingException.class).when(emailService).enviarEmail(anyString(), anyString(), anyString());

        eventPublisher.publishEvent(event);

        verify(emailService, times(1)).enviarEmail(anyString(), anyString(), anyString());
    }
}
