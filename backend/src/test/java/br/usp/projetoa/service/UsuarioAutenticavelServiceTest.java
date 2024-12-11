package br.usp.projetoa.service;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import br.usp.projetoa.dto.JwtTokenDto;
import br.usp.projetoa.dto.LoginDto;
import br.usp.projetoa.enums.RoleEnum;
import br.usp.projetoa.model.Role;
import br.usp.projetoa.model.UsuarioAutenticavel;
import br.usp.projetoa.security.JwtTokenService;
import br.usp.projetoa.security.UserDetailsImpl;

@SpringBootTest(classes = UsuarioAutenticavelService.class)
public class UsuarioAutenticavelServiceTest {
    @MockBean private AuthenticationManager authenticationManager;
    @MockBean private JwtTokenService jwtTokenService;

    @Autowired
    private UsuarioAutenticavelService usuarioAutenticavelService;

    @Test
    @DisplayName("Deve autenticar o usuário e retornar um token JWT com sucesso")
    void deveAutenticarUsuarioERetornarTokenJwtComSucesso() {
        String username = "username";
        String senha = "senha";
        String jwtToken = "jwtToken";

        LoginDto loginDto = new LoginDto(username, senha);

        UsuarioAutenticavel usuarioAutenticavel = mock(UsuarioAutenticavel.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(usuarioAutenticavel);
        Authentication authentication = mock(Authentication.class);

        when(usuarioAutenticavel.getUsername()).thenReturn(username);
        when(usuarioAutenticavel.getPassword()).thenReturn(senha);
        when(usuarioAutenticavel.getRoles()).thenReturn(Set.of(new Role(RoleEnum.ROLE_ALUNO)));

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenService.geraToken(userDetails)).thenReturn(jwtToken);

        JwtTokenDto resultado = usuarioAutenticavelService.authenticateUser(loginDto);

        assertNotNull(resultado);
        assertEquals(jwtToken, resultado.token());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenService, times(1)).geraToken(userDetails);
    }
}
