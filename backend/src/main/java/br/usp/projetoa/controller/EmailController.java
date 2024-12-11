package br.usp.projetoa.controller;

import jakarta.mail.MessagingException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.usp.projetoa.service.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<String> enviarEmail(
            @RequestParam String destinatario,
            @RequestParam String assunto,
            @RequestParam String mensagem
    ) {
        try {
            emailService.enviarEmail(destinatario, assunto, mensagem);

            return ResponseEntity.ok("E-mail enviado com sucesso!");
        } catch (MessagingException ex) {
            ex.printStackTrace();

            return ResponseEntity.internalServerError().body("Erro ao enviar e-mail!");
        }
    }
}
