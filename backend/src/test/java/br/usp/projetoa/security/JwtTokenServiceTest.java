package br.usp.projetoa.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import com.auth0.jwt.exceptions.JWTVerificationException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.usp.projetoa.enums.RoleEnum;
import br.usp.projetoa.model.Role;
import br.usp.projetoa.model.UsuarioAutenticavel;

@SpringBootTest(classes = JwtTokenService.class)
public class JwtTokenServiceTest {
    @Autowired JwtTokenService jwtTokenService;

    @Test
    @DisplayName("Deve gerar um token JWT válido")
    void deveGerarTokenJwtValido() {
        String username = "1";
        Set<Role> roles = Set.of(new Role(RoleEnum.ROLE_ALUNO));

        UsuarioAutenticavel usuarioAutenticavel = mock(UsuarioAutenticavel.class);
        UserDetailsImpl userDetailsImpl = new UserDetailsImpl(usuarioAutenticavel);

        when(usuarioAutenticavel.getUsername()).thenReturn(username);
        when(usuarioAutenticavel.getRoles()).thenReturn(roles);

        String token = jwtTokenService.geraToken(userDetailsImpl);

        assertNotNull(token);
        assertFalse(token.isEmpty());

        String subject = jwtTokenService.extraiSubjectDoToken(token);
        assertEquals(username, subject);
    }

    @Test
    @DisplayName("Deve lançar exceção ao extrair subject de um token inválido")
    void deveLancarExcecaoParaTokenInvalido() {
        String tokenInvalido = "invalido";

        assertThrows(JWTVerificationException.class, () -> jwtTokenService.extraiSubjectDoToken(tokenInvalido));
    }
}
