package br.usp.projetoa.security;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import br.usp.projetoa.enums.RoleEnum;

@SpringBootTest(classes = AuthenticatedUserUtil.class)
public class AuthenticatedUserUtilTest {

    @Mock private Authentication authentication;
    @Mock private UserDetailsImpl userDetails;

    private MockedStatic<SecurityContextHolder> mockedSecurityContextHolder;

    @BeforeEach
    void setup() {
        mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class);

        SecurityContext securityContext = mock(SecurityContext.class);

        when(SecurityContextHolder.getContext()).thenReturn(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @AfterEach
    void tearDown() {
        mockedSecurityContextHolder.close();
    }

    @Nested
    class getNumeroUspLogado {
        @Test
        @DisplayName("Deve retornar o número USP do usuário logado")
        void deveRetornarNumeroUspLogado() {
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(userDetails.getUsername()).thenReturn("1");

            Long numeroUsp = AuthenticatedUserUtil.getNumeroUspLogado();

            assertNotNull(numeroUsp);
            assertEquals(1L, numeroUsp);
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário não está autenticado")
        void deveLancarExcecaoQuandoUsuarioNaoAutenticado() {
            when(authentication.getPrincipal()).thenReturn(null);

            assertThrows(IllegalStateException.class, AuthenticatedUserUtil::getNumeroUspLogado);
        }
    }


    @Nested
    class getRolesLogado {
        @Test
        @DisplayName("Deve retornar as roles do usuário logado")
        void deveRetornarRolesDoUsuarioLogado() {
            Collection<? extends GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority(RoleEnum.ROLE_CCP.toString())
            );

            Mockito.doReturn(authorities).when(authentication).getAuthorities();

            Set<String> roles = AuthenticatedUserUtil.getRolesLogado();

            assertNotNull(roles);
            assertEquals(authorities.size(), roles.size());
            assertTrue(roles.contains(RoleEnum.ROLE_CCP.toString()));
        }

        @Test
        @DisplayName("Deve lançar exceção quando roles não estão disponíveis")
        void deveLancarExcecaoQuandoRolesNaoDisponiveis() {
            when(authentication.getAuthorities()).thenReturn(null);

            assertThrows(IllegalStateException.class, AuthenticatedUserUtil::getRolesLogado);
        }
    }


    @Nested
    class temPermissaoParaAcessarNumeroUsp {
        @Test
        @DisplayName("Deve verificar permissão para acessar com número USP")
        void deveVerificarPermissaoParaAcessarComNumeroUsp() {
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(userDetails.getUsername()).thenReturn("1");

            boolean temPermissao = AuthenticatedUserUtil.temPermissaoParaAcessarNumeroUsp(1L);

            assertTrue(temPermissao);
        }

        @Test
        @DisplayName("Deve retornar falso quando não tem permissão para acessar com número USP")
        void deveRetornarFalsoQuandoNaoTemPermissaoParaAcessarComNumeroUsp() {
            when(authentication.getPrincipal()).thenReturn(userDetails);
            when(userDetails.getUsername()).thenReturn("2");

            boolean temPermissao = AuthenticatedUserUtil.temPermissaoParaAcessarNumeroUsp(1L);

            assertFalse(temPermissao);
        }
    }
}
