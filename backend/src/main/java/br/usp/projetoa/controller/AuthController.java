package br.usp.projetoa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.usp.projetoa.dto.JwtTokenDto;
import br.usp.projetoa.dto.LoginDto;
import br.usp.projetoa.service.UsuarioAutenticavelService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Controller de autenticação", description = "Operações relacionadas à autenticação")
public class AuthController {
    private final UsuarioAutenticavelService usuarioAutenticavelService;

    public AuthController(UsuarioAutenticavelService usuarioAutenticavelService) {
        this.usuarioAutenticavelService = usuarioAutenticavelService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenDto> authenticateUser(@RequestBody LoginDto loginDto) {
        JwtTokenDto token = usuarioAutenticavelService.authenticateUser(loginDto);

        return ResponseEntity.ok(token);
    }
}
