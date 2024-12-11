package br.usp.projetoa.security;

import static org.mockito.ArgumentMatchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import br.usp.projetoa.model.Aluno;
import br.usp.projetoa.model.Docente;
import br.usp.projetoa.repository.AlunoRepository;
import br.usp.projetoa.repository.DocenteRepository;

@SpringBootTest(classes = UserDetailsServiceImpl.class)
public class UserDetailsServiceImplTest {
    @MockBean private AlunoRepository alunoRepository;
    @MockBean private DocenteRepository docenteRepository;

    @Autowired private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    @DisplayName("Deve carregar o aluno com sucesso quando existir")
    void deveCarregarAlunoComSucessoQuandoExistir() {
        Aluno aluno = new Aluno();

        when(alunoRepository.findById(anyLong())).thenReturn(Optional.of(aluno));

        UserDetails output = userDetailsServiceImpl.loadUserByUsername("1");

        assertNotNull(output);
    }

    @Test
    @DisplayName("Deve carregar o docente com sucesso quando existir")
    void deveCarregarDocenteComSucessoQuandoExistir() {
        Docente docente = new Docente();

        when(alunoRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(docenteRepository.findById(anyLong())).thenReturn(Optional.of(docente));

        UserDetails output = userDetailsServiceImpl.loadUserByUsername("1");

        assertNotNull(output);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existir")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        when(alunoRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(docenteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userDetailsServiceImpl.loadUserByUsername("1"));
    }
}
