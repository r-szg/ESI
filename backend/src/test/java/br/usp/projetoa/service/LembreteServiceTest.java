package br.usp.projetoa.service;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import jakarta.mail.MessagingException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.usp.projetoa.enums.SituacaoEntregaRelatorio;
import br.usp.projetoa.enums.TipoCurso;
import br.usp.projetoa.model.Aluno;
import br.usp.projetoa.model.StatusEntregaRelatorio;
import br.usp.projetoa.repository.StatusEntregaRelatorioRepository;

@SpringBootTest(classes = LembreteService.class)
public class LembreteServiceTest {
    @MockBean private StatusEntregaRelatorioRepository statusEntregaRelatorioRepository;
    @MockBean private EmailService emailService;

    @Autowired private LembreteService lembreteService;

    @Test
    @DisplayName("Deve enviar lembrete apenas para relatórios pendentes e sem lembrete recente")
    void deveEnviarLembreteApenasParaRelatoriosPendentesESemLembreteRecente() throws MessagingException {
        Aluno aluno = new Aluno(
            1L, 
            "aluno", 
            "email@email.com", 
            "senha", 
            LocalDate.now(), 
            "rg", 
            "localNascimento", 
            "nacionalidade", 
            TipoCurso.MESTRADO, 
            "linkLattes", 
            LocalDate.now(), 
            LocalDate.now(), 
            LocalDate.now(), 
            LocalDate.now()
        );

        StatusEntregaRelatorio statusEntregaRelatorio1 = new StatusEntregaRelatorio("2S2024", SituacaoEntregaRelatorio.PENDENTE);
        statusEntregaRelatorio1.setAluno(aluno);
        statusEntregaRelatorio1.setDataUltimoLembrete(null);

        StatusEntregaRelatorio statusEntregaRelatorio2 = new StatusEntregaRelatorio("2S2024", SituacaoEntregaRelatorio.PENDENTE);
        statusEntregaRelatorio2.setAluno(aluno);
        statusEntregaRelatorio2.setDataUltimoLembrete(LocalDate.now().minusDays(31));

        StatusEntregaRelatorio statusEntregaRelatorio3 = new StatusEntregaRelatorio("2S2024", SituacaoEntregaRelatorio.PENDENTE);
        statusEntregaRelatorio3.setAluno(aluno);
        statusEntregaRelatorio3.setDataUltimoLembrete(LocalDate.now().minusDays(5));

        List<StatusEntregaRelatorio> pendentes = List.of(
            statusEntregaRelatorio1, 
            statusEntregaRelatorio2, 
            statusEntregaRelatorio3);

        when(statusEntregaRelatorioRepository
            .findBySemestreAnoAndSituacaoEntregaRelatorio(anyString(), eq(SituacaoEntregaRelatorio.PENDENTE)))
            .thenReturn(pendentes);

        lembreteService.enviarLembretes();

        verify(emailService, times(2)).enviarEmail(anyString(), anyString(), anyString());

        assertEquals(LocalDate.now(), statusEntregaRelatorio1.getDataUltimoLembrete());
        assertEquals(LocalDate.now(), statusEntregaRelatorio2.getDataUltimoLembrete());
        assertNotEquals(LocalDate.now(), statusEntregaRelatorio3.getDataUltimoLembrete());
    }
}
