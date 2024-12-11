package br.usp.projetoa.service;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest(classes = EmailService.class)
public class EmailServiceTest {
    @MockBean private JavaMailSender mailSender;

    @Autowired private EmailService emailService;

    @Captor private ArgumentCaptor<MimeMessage> mimeMessageArgumentCaptor;

    @Test
    @DisplayName("Deve enviar um email com sucesso")
    void deveEnviarEmailComSucesso() throws MessagingException {
        String para = "para";
        String assunto = "assunto";
        String mensagem = "mensagem";

        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        emailService.enviarEmail(para, assunto, mensagem);

        verify(mailSender, times(1)).createMimeMessage();
        verify(mailSender, times(1)).send(mimeMessageArgumentCaptor.capture());

        MimeMessage emailEnviado = mimeMessageArgumentCaptor.getValue();
        assertNotNull(emailEnviado);
    }

    @Test
    @DisplayName("Deve lançar exceção ao falhar no envio")
    void deveLancarExcecaoQuandoFalharNoEnvio() throws MessagingException {
        String para = "para";
        String assunto = "assunto";
        String mensagem = "mensagem";

        MimeMessage mimeMessage = mock(MimeMessage.class);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new RuntimeException("Simulação de erro no envio")).when(mailSender).send(any(MimeMessage.class));

        assertThrows(RuntimeException.class, () -> emailService.enviarEmail(para, assunto, mensagem));
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }
}
