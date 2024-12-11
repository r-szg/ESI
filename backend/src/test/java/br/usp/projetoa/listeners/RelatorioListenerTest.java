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

import br.usp.projetoa.events.RelatorioCriadoEvent;
import br.usp.projetoa.model.Aluno;
import br.usp.projetoa.model.Docente;
import br.usp.projetoa.model.Relatorio;
import br.usp.projetoa.service.EmailService;

@SpringBootTest(classes = RelatorioListener.class)
public class RelatorioListenerTest {
    @MockBean private EmailService emailService;

    @Autowired private ApplicationEventPublisher eventPublisher;

    @Test
    @DisplayName("Deve capturar o evento e enviar um email com sucesso")
    void deveCapturarEventoEEnviarEmailComSucesso() throws MessagingException {
        Aluno aluno = new Aluno();
        aluno.setNumeroUsp(1L);
        aluno.setNomeCompleto("aluno");

        Docente docente = new Docente();
        docente.setEmail("docente@email.com");

        Relatorio relatorio = new Relatorio();
        relatorio.setAluno(aluno);
        relatorio.setDocente(docente);

        RelatorioCriadoEvent event = new RelatorioCriadoEvent(relatorio);

        eventPublisher.publishEvent(event);

        verify(emailService, times(1)).enviarEmail(
            eq(docente.getEmail()), 
            eq("Envio de relatório"), 
            contains(Long.toString(aluno.getNumeroUsp()))
        );
    }

    @Test
    @DisplayName("Deve tratar exceção ao enviar email")
    void deveTratarExcecaoAoEnviarEmail() throws MessagingException {
        Aluno aluno = new Aluno();
        aluno.setNumeroUsp(1L);
        aluno.setNomeCompleto("aluno");

        Docente docente = new Docente();
        docente.setEmail("docente@email.com");

        Relatorio relatorio = new Relatorio();
        relatorio.setAluno(aluno);
        relatorio.setDocente(docente);

        RelatorioCriadoEvent event = new RelatorioCriadoEvent(relatorio);

        doThrow(MessagingException.class).when(emailService).enviarEmail(anyString(), anyString(), anyString());

        eventPublisher.publishEvent(event);

        verify(emailService, times(1)).enviarEmail(anyString(), anyString(), anyString());
    }
}
